--1a
DROP VIEW IF EXISTS account_v;

CREATE OR REPLACE VIEW account_v
AS
	WITH account AS (
		SELECT o_trader AS trader,o_stock AS stock, d_amount AS amount,o_amount AS call_amount, o_limit AS call_limit,
				null AS put_amount, null AS put_limit, m_price AS market_price, m_price * o_amount AS market_value
		FROM orders
		LEFT JOIN depots
		ON  o_trader = d_trader
		AND o_stock = d_stock 
		INNER  JOIN market
		ON  o_stock = m_stock
		WHERE  o_type = 'CALL'
		UNION
		SELECT o_trader AS trader,o_stock AS stock, d_amount AS amount,null AS call_amount, null AS call_limit,
			o_amount AS put_amount, o_limit AS put_limit, m_price AS market_price, m_price * o_amount AS market_value
		FROM orders
		INNER  JOIN depots
		ON  o_trader = d_trader
		AND o_stock = d_stock 
		INNER  JOIN market
		ON  o_stock = m_stock
		WHERE  o_type = 'PUT'
		ORDER BY trader ASC
	)
	select * from account;

	
--1b
DROP VIEW IF EXISTS orders_v;

CREATE VIEW orders_v
AS
	WITH call_orders AS (SELECT * FROM orders WHERE  o_type = 'CALL'),
	        put_orders AS (SELECT * FROM orders WHERE  o_type = 'PUT'),
	        orders_volume AS (SELECT o.o_stock AS stock,
					         o.o_limit AS price,
					        (SELECT sum(o_amount) FROM call_orders
					        WHERE  o_limit >= o.o_limit AND o_stock= o.o_stock ) AS call_volume,
					        (SELECT sum(o_amount) FROM put_orders
					        WHERE  o_limit <= o.o_limit AND o_stock= o.o_stock ) AS put_volume
					        FROM orders o)

	SELECT DISTINCT stock, call_volume,
						 CASE WHEN call_volume > put_volume THEN (call_volume - put_volume)
						 	  WHEN put_volume IS NULL THEN call_volume
					          ELSE 0
					     END AS call_backlog,
					price,
						CASE WHEN put_volume > call_volume THEN (put_volume - call_volume)
							 WHEN call_volume IS NULL THEN put_volume
					          ELSE 0
					     END AS put_backlog,
					put_volume
	FROM orders_volume
	ORDER BY price desc ;

--2a
CREATE OR REPLACE FUNCTION account_v_insert()
RETURNS trigger AS
$BODY$
DECLARE
	call_total FLOAT;
	balance FLOAT;
	depot_amount FLOAT;
	amount_stocks integer;
	v_state   TEXT;
    v_msg     TEXT;
    v_context TEXT;
BEGIN
	IF pg_trigger_depth() <> 1 THEN
        RETURN NEW;
    END IF;

	select COUNT(*) from account_v av
	into amount_stocks
	where av.stock = NEW.stock and av.trader = NEW.trader;

	IF NEW.put_amount IS NOT NULL and NEW.put_amount <> 0 THEN
		select d.d_amount into depot_amount from depots d
		where d.d_trader = NEW.trader and d_stock = NEW.stock;

		IF depot_amount IS NULL THEN
			RAISE EXCEPTION SQLSTATE '70001' USING message = 'Stock does not exist for Trader';
		END IF;

		IF NEW.put_amount > depot_amount THEN
			RAISE EXCEPTION SQLSTATE '70001' USING message = 'Put Amount is greater than amount in depot';
		END IF;
	END IF;

	IF NEW.call_amount IS NOT NULL AND NEW.call_amount <> 0 THEN
		select traders.t_balance into balance from traders where t_id = NEW.trader;
		call_total := NEW.call_amount * NEW.call_limit;

		IF balance IS NULL THEN
			RAISE EXCEPTION SQLSTATE '70002' USING message = 'Trader does not exist';
		END IF;

		IF call_total > balance THEN
			RAISE EXCEPTION SQLSTATE '70002' USING message = 'Insufficient Funds to Buy Stocks';
		END IF;
	END IF;

	IF amount_stocks >= 1 THEN
		RAISE EXCEPTION SQLSTATE '70003' USING message = 'You can only place one order per stock';
	END IF;

	IF NEW.call_amount < 0 OR NEW.call_limit < 0
		OR NEW.put_amount < 0 OR NEW.put_limit < 0 THEN
			RAISE EXCEPTION SQLSTATE '70004' USING message = 'Amount or limit must be greater or equal to 0';
	ELSE
		IF NEW.call_amount = 0 AND NEW.call_limit = 0 THEN
			IF NEW.put_amount = 0 or NEW.put_limit = 0 THEN
				RAISE EXCEPTION SQLSTATE '70004' USING message = 'Put and call cannot both be 0. i.e. empty order';
			END IF;
		END IF;

		IF NEW.put_amount = 0 AND NEW.put_limit = 0 THEN
			IF NEW.call_amount = 0 or NEW.call_limit = 0 THEN
				RAISE EXCEPTION SQLSTATE '70004' USING message = 'Put and call cannot both be 0. i.e. empty order';
			END IF;
		END IF;

		IF NEW.put_amount > 0 and NEW.put_limit > 0 THEN
			IF NEW.call_amount > 0 or NEW.call_limit > 0 THEN
				RAISE EXCEPTION SQLSTATE '70005' USING message = 'Put and call cannot both be above 0.';
			END IF;
		END IF;

		IF NEW.call_amount > 0 and NEW.call_limit > 0 THEN
			IF NEW.put_amount > 0 or NEW.put_limit > 0 THEN
				RAISE EXCEPTION SQLSTATE '70005' USING message = 'Put and call cannot both be above 0.';
			END IF;
		END IF;
		IF NEW.market_value IS NOT NULL or NEW.market_price IS NOT NULL THEN
			RAISE EXCEPTION SQLSTATE '70006' USING message = 'Market value and price are not editable';
		END IF;
		IF NEW.amount IS NOT NULL THEN
			RAISE EXCEPTION SQLSTATE '70006' USING message = 'insertion of amount is prohibited';
		END IF;
	END IF;

	IF NEW.put_amount > 0 THEN
		INSERT INTO orders(o_type, o_trader, o_amount, o_stock, o_limit)
		VALUES('PUT', NEW.trader, NEW.put_amount, NEW.stock, NEW.put_limit);
	ELSE
		INSERT INTO orders(o_type, o_trader, o_amount, o_stock, o_limit)
		VALUES('CALL', NEW.trader, NEW.call_amount, NEW.stock, NEW.call_limit);

		UPDATE traders
		SET t_balance = t_balance - call_total
		WHERE t_id = NEW.trader;
	END IF;

	RETURN NEW;
EXCEPTION
    WHEN OTHERS THEN
		get stacked diagnostics
			v_state   = returned_sqlstate,
			v_msg     = message_text,
			v_context = pg_exception_context;

		raise notice E'An Error Occured:
		state  : %
		message: %
		context: %', v_state, v_msg, v_context;
		return NULL;
END;
$BODY$
LANGUAGE PLPGSQL;

DROP TRIGGER IF EXISTS account_v_insert ON account_v;
CREATE TRIGGER account_v_insert
INSTEAD OF insert ON account_v
FOR EACH ROW
  	EXECUTE PROCEDURE account_v_insert();

--2b
CREATE OR REPLACE FUNCTION account_v_delete()
RETURNS trigger AS
$BODY$
BEGIN
	IF pg_trigger_depth() <> 1 THEN
        RETURN NEW;
    END IF;

	IF (TG_OP = 'DELETE') THEN
		DELETE FROM orders
		WHERE o_trader = OLD.trader AND o_stock = OLD.stock;
	END IF;

	RETURN OLD;
END;
$BODY$
LANGUAGE PLPGSQL;

DROP TRIGGER IF EXISTS account_v_delete ON account_v;
CREATE TRIGGER account_v_delete
INSTEAD OF DELETE ON account_v
FOR EACH ROW
  EXECUTE PROCEDURE account_v_delete();

--3
CREATE OR REPLACE PROCEDURE calculate_prices(
	INOUT result_ refcursor = 'rs_resultone'
)
 AS
$BODY$
DECLARE
	temprow record;
	largest_stock_volume integer;
	call_volume_order integer;
	total_volume integer;
	put_volume_order integer;
	max_stock_price FLOAT;
	min_stock_price FLOAT;
	count_largest_volume integer;
	call_backlog integer;
	put_backlog integer;
	market_value FLOAT;
	stock_price record;
	global_m_time integer;
BEGIN
	FOR temprow IN
			select DISTINCT s.stock from orders_v s
		LOOP
			--Selects the limit that yeild the highest trading volume. .i.e  least difference between
			--put and call volume
			SELECT MIN(largest_volume) into largest_stock_volume from (
				SELECT o.stock,o.call_volume,o.price,o.put_volume,
				o.put_backlog,o.call_backlog,ABS(o.put_volume - o.call_volume) as largest_volume,
				o.put_volume + o.call_volume as largest_volume_amount
				FROM orders_v o
				where o.stock = temprow.stock
			) stock_price;

			--select the largest volume of the limit(s) that yeild the highest trading volume.
			SELECT MAX(largest_volume_amount) into total_volume from (
				SELECT o.stock,o.call_volume,o.price,o.put_volume,
				o.put_backlog,o.call_backlog,ABS(o.put_volume - o.call_volume) as largest_volume,
				o.put_volume + o.call_volume as largest_volume_amount
				FROM orders_v o
				where o.stock = temprow.stock
			) stock_price
			where largest_volume = largest_stock_volume;

			--sum the put backlog for the limits that yeild the highest trading volume
			SELECT SUM(s.put_backlog) into put_backlog FROM (
				SELECT o.stock,o.call_volume,o.price,o.put_volume,
				o.put_backlog,o.call_backlog,ABS(o.put_volume - o.call_volume) as largest_volume,
				o.put_volume + o.call_volume as largest_volume_amount
				FROM orders_v o
				where o.stock = temprow.stock
			) s
			where largest_volume = largest_stock_volume
			and largest_volume_amount = total_volume;

			--sum the call backlog for the limits that yeild the highest trading volume
			SELECT SUM(s.call_backlog) into call_backlog FROM (
				SELECT o.stock,o.call_volume,o.price,o.put_volume,
				o.put_backlog,o.call_backlog,ABS(o.put_volume - o.call_volume) as largest_volume,
				o.put_volume + o.call_volume as largest_volume_amount
				FROM orders_v o
				where o.stock = temprow.stock
			) s
			where largest_volume = largest_stock_volume
			and largest_volume_amount = total_volume;

			--get highest price for the limits that yeild the highest trading volume
			SELECT MAX(price) into max_stock_price FROM (
				SELECT o.stock,o.call_volume,o.price,o.put_volume,
				o.put_backlog,o.call_backlog,ABS(o.put_volume - o.call_volume) as largest_volume,
				o.put_volume + o.call_volume as largest_volume_amount
				FROM orders_v o
				where o.stock = temprow.stock
			) s where largest_volume = largest_stock_volume and
			largest_volume_amount = total_volume;

			--get lowest price for the limits that yeild the highest trading volume
			SELECT MIN(price) into min_stock_price FROM (
				SELECT o.stock,o.call_volume,o.price,o.put_volume,
				o.put_backlog,o.call_backlog,ABS(o.put_volume - o.call_volume) as largest_volume,
				o.put_volume + o.call_volume as largest_volume_amount
				FROM orders_v o
				where o.stock = temprow.stock
			) s where
			largest_volume = largest_stock_volume and
			largest_volume_amount = total_volume;

			--get market price of particular stock
			SELECT ac.market_price INTO market_value FROM account_v ac
			where ac.stock = temprow.stock
			FETCH FIRST ROW ONLY;

			--get number of limits that yeild the highest trading volume
			SELECT COUNT(*) into count_largest_volume FROM (
				SELECT o.stock,o.call_volume,o.price,o.put_volume,
				o.put_backlog,o.call_backlog,ABS(o.put_volume - o.call_volume) as largest_volume,
				o.put_volume + o.call_volume as largest_volume_amount
				FROM orders_v o
				where o.stock = temprow.stock
			) s where
			largest_volume = largest_stock_volume and largest_volume_amount = total_volume;


			--rule p1
			IF count_largest_volume = 1 THEN
				select MAX(m_time) into global_m_time from market m;
				UPDATE market m
				SET m_price = min_stock_price, m_time = global_m_time + 1
				WHERE m_stock = temprow.stock;
			END IF;

			IF count_largest_volume > 1 THEN
			    --rule p3
				IF put_backlog > call_backlog THEN
					select MAX(m_time) into global_m_time from market m;
					UPDATE market m
					SET m_price = min_stock_price, m_time = global_m_time + 1
					WHERE m.m_stock = temprow.stock;
				END IF;
                --rule p2
				IF call_backlog > put_backlog THEN
					select MAX(m_time) into global_m_time from market m;
					UPDATE market m
					SET m_price = max_stock_price, m_time = global_m_time + 1
					WHERE m.m_stock = temprow.stock;
				END IF;
                --rule p4
				IF put_backlog = call_backlog THEN
					IF market_value >= max_stock_price THEN
						select MAX(m_time) into global_m_time from market m;
						UPDATE market m
						SET m_price = max_stock_price, m_time = global_m_time + 1
						WHERE m.m_stock = temprow.stock;
					END IF;

					IF market_value <= min_stock_price THEN
						select MAX(m_time) into global_m_time from market m;
						UPDATE market m
						SET m_price = min_stock_price, m_time = global_m_time + 1
						WHERE m.m_stock = temprow.stock;
					END IF;
				END IF;
                --rule p5
				IF put_backlog is null and call_backlog is null THEN
					IF market_value >= max_stock_price THEN
						select MAX(m_time) into global_m_time from market m;
						UPDATE market m
						SET m_price = max_stock_price, m_time = global_m_time + 1
						WHERE m.m_stock = temprow.stock;
					END IF;

					IF market_value <= min_stock_price THEN
						select MAX(m_time) into global_m_time from market m;
						UPDATE market m
						SET m_price = min_stock_price, m_time = global_m_time + 1
						WHERE m.m_stock = temprow.stock;
					END IF;
				END IF;
			END IF;

			--get limits that have call orders but can't be traded
 			SELECT SUM(o.call_volume - o.call_backlog) into call_volume_order
 			FROM (
				SELECT o.stock,o.call_volume,o.price,o.put_volume,
				o.put_backlog,o.call_backlog,ABS(o.put_volume - o.call_volume) as largest_volume,
				o.put_volume + o.call_volume as largest_volume_amount
				FROM orders_v o
				where o.stock = temprow.stock
			) o;

			--get limits that have put orders but can't be traded
 			SELECT SUM(o.put_volume - o.put_backlog) into put_volume_order
 			FROM (
				SELECT o.stock,o.call_volume,o.price,o.put_volume,
				o.put_backlog,o.call_backlog,ABS(o.put_volume - o.call_volume) as largest_volume,
				o.put_volume + o.call_volume as largest_volume_amount
				FROM orders_v o
				where o.stock = temprow.stock
			) o;

 			SELECT MAX(price) into max_stock_price FROM (
				SELECT o.stock,o.call_volume,o.price,o.put_volume,
				o.put_backlog,o.call_backlog,ABS(o.put_volume - o.call_volume) as largest_volume,
				o.put_volume + o.call_volume as largest_volume_amount
				FROM orders_v o
				where o.stock = temprow.stock
			) o;

 			SELECT MIN(price) into min_stock_price FROM (
				SELECT o.stock,o.call_volume,o.price,o.put_volume,
				o.put_backlog,o.call_backlog,ABS(o.put_volume - o.call_volume) as largest_volume,
				o.put_volume + o.call_volume as largest_volume_amount
				FROM orders_v o
				where o.stock = temprow.stock
			) o;

			--rule p6
 			IF call_volume_order = 0 and put_volume_order = 0 THEN
 				IF market_value >= max_stock_price THEN
					select MAX(m_time) into global_m_time from market m;
 					UPDATE market m
 					SET m_price = max_stock_price, m_time = global_m_time + 1
 					WHERE m.m_stock = temprow.stock;
 				END IF;

 				IF market_value <= min_stock_price THEN
					select MAX(m_time) into global_m_time from market m;
 					UPDATE market m
 					SET m_price = min_stock_price, m_time = global_m_time + 1
 					WHERE m.m_stock = temprow.stock;
 				END IF;
 			END IF;

		END LOOP;
-- 	RETURN query
		open result_ for
			select m.m_stock, m.m_price from market m
 			order by m.m_stock asc;
END;
$BODY$
LANGUAGE PLPGSQL;
CALL calculate_prices();
    FETCH ALL FROM "rs_resultone";

--4

DROP PROCEDURE IF EXISTS trade(INOUT rc refcursor);
CREATE OR REPLACE PROCEDURE trade(INOUT rc refcursor) 
LANGUAGE plpgsql 
AS $$
DECLARE
    orderrow RECORD;
   	putvol integer;
    callvol integer;
    marketprice integer;
    currentsumamount integer := 0 ;
   	prevordertype varchar;
    prevorderstock varchar;
    remcallamount integer;
    remputamount integer;
   	delrowid integer[];
    updaterow integer[][];
    x integer[];
BEGIN
     
    FOR orderrow IN SELECT * 
	       FROM orders 
	       ORDER BY o_id 
    LOOP 
    	
		putvol:= (SELECT 
				sum(o_amount)
				FROM orders
				INNER JOIN market 
				ON o_stock = m_stock 
				WHERE o_limit <= m_price AND o_type = 'PUT' AND o_stock = orderrow.o_stock);
				
		callvol:= (SELECT 
				sum(o_amount)
				FROM orders 
				INNER JOIN market
				ON o_stock = m_stock 
				WHERE o_limit >= m_price AND o_type = 'CALL' AND o_stock = orderrow.o_stock);
	
		marketprice := (SELECT m_price FROM market WHERE m_stock = orderrow.o_stock);
	
		prevordertype := (SELECT o_type FROM orders WHERE o_id = orderrow.o_id -1);
		prevorderstock := (SELECT o_stock FROM orders WHERE o_id = orderrow.o_id -1);
		
		IF prevordertype = orderrow.o_type AND prevorderstock = orderrow.o_stock THEN 
			currentsumamount := currentsumamount + orderrow.o_amount;
		ELSE
			currentsumamount := 0;
			currentsumamount := currentsumamount + orderrow.o_amount;
		END IF;
	
		remcallamount := currentsumamount - callvol;
		remputamount := currentsumamount - putvol ;
	  
	  	IF putvol = callvol THEN 
			IF (orderrow.o_type = 'PUT' AND orderrow.o_limit <= marketprice) THEN 
				UPDATE traders 
				SET t_balance = t_balance + (marketprice * orderrow.o_amount)
				WHERE t_id = orderrow.o_trader;
				
				delrowid:= array_append(delrowid,orderrow.o_id);
			ELSIF (orderrow.o_type = 'CALL' AND orderrow.o_limit >= marketprice) THEN 
				UPDATE traders 
				SET t_balance = t_balance - (marketprice * orderrow.o_amount)
				WHERE t_id = orderrow.o_trader;
			
				delrowid:= array_append(delrowid,orderrow.o_id);
			END IF;

		ELSIF putvol > callvol THEN 
			IF (orderrow.o_type = 'PUT' AND orderrow.o_limit <= marketprice) THEN
				IF currentsumamount > callvol THEN --For orders that will stay in the system
					IF remcallamount < orderrow.o_amount THEN --Check if remaining vol is less than amount of shares
						UPDATE traders 
						SET t_balance = t_balance + (marketprice * (orderrow.o_amount-remcallamount))
						WHERE t_id = orderrow.o_trader; 
						
						updaterow:= updaterow || ARRAY[ARRAY[orderrow.o_id,remcallamount]];
					END IF;
				ELSE -- currentsumamount <= callvol
					UPDATE traders 
					SET t_balance = t_balance + (marketprice * orderrow.o_amount)
					WHERE t_id = orderrow.o_trader; 
					
					delrowid:= array_append(delrowid,orderrow.o_id);
				END IF;
			ELSIF (orderrow.o_type = 'CALL' AND orderrow.o_limit >= marketprice) THEN
				UPDATE traders 
				SET t_balance = t_balance - (marketprice * orderrow.o_amount)
				WHERE t_id = orderrow.o_trader;
			
				delrowid:= array_append(delrowid,orderrow.o_id);				
			END IF;		
	
		ELSIF callvol > putvol THEN 
			IF (orderrow.o_type = 'PUT' AND orderrow.o_limit <= marketprice) THEN
				UPDATE traders 
				SET t_balance = t_balance + (marketprice * orderrow.o_amount)
				WHERE t_id = orderrow.o_trader;
			
				delrowid:= array_append(delrowid,orderrow.o_id);			
			ELSIF (orderrow.o_type = 'CALL' AND orderrow.o_limit >= marketprice) THEN
				IF currentsumamount > putvol THEN --For orders that will stay in the system
					IF remputamount < orderrow.o_amount THEN --Check if remaining vol is less than amount of shares
						UPDATE traders 
						SET t_balance = t_balance - (marketprice * (orderrow.o_amount-remputamount))
						WHERE t_id = orderrow.o_trader; 
						
						updaterow:= updaterow || ARRAY[ARRAY[orderrow.o_id,remputamount]];					
					END IF;
				ELSE -- currentsumamount <= putvol
					UPDATE traders 
					SET t_balance = t_balance - (marketprice * orderrow.o_amount)
					WHERE t_id = orderrow.o_trader; 
					
					delrowid:= array_append(delrowid,orderrow.o_id);
				END IF;
			END IF;					
		END IF;			
    END LOOP;
   --Make changes to order table
   	IF (updaterow IS NOT NULL) THEN 
	    FOREACH x SLICE 1 IN ARRAY updaterow
		  LOOP
		    UPDATE orders
		    SET o_amount = x[2]
		    WHERE o_id = x[1];
		  END LOOP;
	 END IF;
   	
	IF (delrowid IS NOT NULL) THEN 
  	  DELETE FROM orders
   	  WHERE o_id = ANY (delrowid);
   	END IF;
   	 
	rc := 'trade_cursor';
   	OPEN rc FOR SELECT t_name,t_balance FROM traders ORDER BY t_name ;
END;
$$;	





	

		

