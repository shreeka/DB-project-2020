-- 3a)
CREATE OR REPLACE FUNCTION formataddress(addid INTEGER) RETURNS VARCHAR AS $emailid$
DECLARE 
	emailid VARCHAR ;
BEGIN
	SELECT concat(username,'@',domain) INTO emailid FROM address WHERE adid = addid;
	RETURN emailid ;
END; $emailid$
LANGUAGE PLPGSQL;

--3b)
CREATE OR REPLACE FUNCTION formatuser(userid INTEGER) RETURNS VARCHAR AS $user$
DECLARE 
	user VARCHAR ;
BEGIN
	IF userid IN (SELECT euid FROM person) THEN
		SELECT concat(first_name,' ',surname,'(',nickname,')') INTO user FROM persON WHERE euid = userid ; 
	ELSIF userid IN (SELECT euid FROM organization) THEN
		SELECT concat(firm,'(',nickname,')') INTO user FROM organization WHERE euid = userid;
	ELSE
		SELECT nickname INTO user FROM euser WHERE euid = userid ;
	END IF;
RETURN user;	
END; $user$
LANGUAGE PLPGSQL;

--3c
CREATE OR REPLACE FUNCTION addressbook (userid INTEGER) 
	RETURNS TABLE (
		username VARCHAR,
		emailid VARCHAR
) 
AS $$
BEGIN
	RETURN query WITH senderaddress AS (SELECT 
							_from AS adr,a1.email
							FROM email e 
							INNER JOIN addressee a1 
							ON e.id  = a1.email OR e.in_reply_to = a1.email 
							INNER JOIN use u 
							ON a1."_to" = u.address 
							WHERE u.euser = userid),
					recipientaddress AS (SELECT
							_to as adr,a2.email
							FROM addressee a2 
							INNER JOIN email e2
							ON  e2.id = a2.email OR e2.in_reply_to = a2.email 
							INNER JOIN use u2
							ON u2.address = e2."_from" 
							WHERE u2.euser = userid),
					sr as (SELECT * FROM senderaddress UNION SELECT * FROM recipientaddress),
					totalsr as (SELECT adr FROM sr 
								UNION
								SELECT t.adr FROM sr t INNER JOIN email e ON e.contained_in = t.email),				
					users AS (SELECT
							u.euser,a.adid 
							FROM use u 
							INNER JOIN address a
							ON a.adid = u.address),
					senderReciever AS (SELECT 
							formatuser(euser), formataddress(adr)
							FROM users us
							INNER JOIN totalsr tsr
							ON tsr.adr = us.adid),
					selfuser AS (SELECT
							formatuser(euser),formataddress(adid)
							FROM users 
							WHERE euser = userid)
					
										 						
					SELECT * FROM senderReciever
					UNION
					SELECT * FROM selfuser;
				
	
END; $$ 
LANGUAGE 'plpgsql';













