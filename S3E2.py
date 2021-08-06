import psycopg2
from psycopg2 import Error
import logging
import time
import resource
import json
import uuid

connection = psycopg2.connect(
    user="postgres",
    password="password",
    host="localhost",
    port="5432"
)


connection.autocommit = False
cursor = connection.cursor()


def getURL(filename):
    return '/Users/i521529/Downloads/120316_801703_bundle_archive/%s' % filename

def setup_logger(name, log_file, level=logging.INFO):

    handler = logging.FileHandler(log_file)
    logger = logging.getLogger(name)
    logger.setLevel(level)
    logger.addHandler(handler)
    return logger

def trim_spaces(x): return (" ".join(x.split()))


#----------Insert titles------------------------------------------
def insert_title_from_dump(file):

    try:
        cur = connection.cursor()
        staging_table = """CREATE TABLE movie_dump (
                        movie_id varchar,
                        title_type varchar,
                        title_name varchar,
                        original_title varchar,
                        adult_movie smallint,
                        release_year smallint,
                        end_year smallint,
                        run_time integer,
                        genre varchar
                        )"""
        cur.execute(staging_table)

        copy_data_to_staging = "COPY movie_dump FROM '{0}' (FORMAT CSV, DELIMITER E'\t', " \
                               "NULL '\\N', HEADER true, QUOTE E'\b')".format(
                                   getURL(file + '/' + file))
        cur.execute(copy_data_to_staging)

        insert_from_staging_table = """INSERT into movie_titles
                                        SELECT movie_id,title_type,title_name,original_title,adult_movie
                                        from movie_dump
                                        """
        cur.execute(insert_from_staging_table)

        connection.commit()

    except (Exception, psycopg2.DatabaseError) as error:
        connection.rollback()
        print(error)


def insert_normalised_title_from_dump():
    try:
        cur = connection.cursor()

        insert_release_info_from_staging_table = """INSERT into release_information
                                        SELECT d.movie_id, d.release_year,d.end_year,d.run_time from movie_dump d
                                        inner join movie_titles m
                                        on m.id = d.movie_id
                                        where d.release_year IS NOT NULL"""
        cur.execute(insert_release_info_from_staging_table)

        insert_genre_from_staging_table = """INSERT into genre
        SELECT movie_id, regexp_split_to_table(genre, E ',') AS
        genre FROM movie_dump"""
        cur.execute(insert_genre_from_staging_table)

        add_fk_constraint_to_release_info = """ALTER TABLE release_information
                                                ADD CONSTRAINT fk_title_id
                                                FOREIGN KEY (title_id) REFERENCES movie_titles (id)"""
        add_fk_constraint_to_genre = """ALTER TABLE genre
                                        ADD CONSTRAINT fk_title_id
                                        FOREIGN KEY (title_id) REFERENCES movie_titles (id)"""

        cur.execute(add_fk_constraint_to_release_info)
        cur.execute(add_fk_constraint_to_genre)

        connection.commit()

    except (Exception, psycopg2.DatabaseError) as error:
        connection.rollback()
        print(error)
#----------Insert titles----------------------------------------

#------------Insert ratings----------------------------------------


def insert_rating_from_dump(file):
    try:
        cur = connection.cursor()
        staging_table = """CREATE TABLE rating_dump (
                        movie_title varchar ,
                        average_rating float8,
                        no_votes integer
                        )"""
        cur.execute(staging_table)

        copy_data_to_staging = "COPY rating_dump FROM '{0}' (FORMAT CSV, DELIMITER E'\t', " \
                               "NULL '\\N', HEADER true, QUOTE E'\b')".format(
                                   getURL(file + '/' + file))
        cur.execute(copy_data_to_staging)

        insert_from_staging_table = """INSERT into ratings
                                        SELECT d.movie_title, d.average_rating, d.no_votes from rating_dump d
                                        inner join movie_titles m
                                        on m.id = d.movie_title"""
        cur.execute(insert_from_staging_table)

        add_fk_constraint_to_table = """ALTER TABLE ratings
                                        ADD CONSTRAINT fk_movie_title
                                        FOREIGN KEY (movie_title) REFERENCES movie_titles (id)"""
        cur.execute(add_fk_constraint_to_table)

        rejected_fk_rows = """SELECT * from rating_dump t1
                            LEFT JOIN movie_titles t2 ON t2.id = t1.movie_title
                            WHERE t2.id IS NULL"""
        cur.execute(rejected_fk_rows)

        for row in cur:
            rejected_count += 1
            logging.basicConfig(filename="./rejected_ratings.log",
                                filemode='a', level=logging.DEBUG)
            logging.info(row)
            logging.error(error)

        connection.commit()

    except (Exception, psycopg2.DatabaseError) as error:
        connection.rollback()
        print(error)
#------------Insert ratings------------------

#------------Insert alias------------------


def insert_alias_from_dump(file):
    rejected_count = 0
    try:
        cur = connection.cursor()
        staging_table = """CREATE TABLE alias_dump (
                        title_id varchar ,
                        ordering varchar,
                        alias_title varchar,
                        region varchar,
                        language varchar ,
                        types varchar ,
                        attributes varchar ,
                        is_original varchar)"""
        cur.execute(staging_table)

        copy_data_to_staging = "COPY alias_dump FROM '{0}' (FORMAT CSV, DELIMITER E'\t', " \
                               "NULL '\\N', HEADER true, QUOTE E'\b')".format(
                                   getURL(file + '/' + file))
        cur.execute(copy_data_to_staging)

        insert_from_staging_table = """INSERT into alias_title(title_id, ordering, alias_title, is_original)
                                        SELECT a.title_id, a.ordering::int, a.alias_title, a.is_original::int from alias_dump a
                                        inner join movie_titles m
                                        on m.id = a.title_id"""
        cur.execute(insert_from_staging_table)

        add_fk_constraint_to_table = """ALTER TABLE alias_title
                                        ADD CONSTRAINT fk_title_id
                                        FOREIGN KEY (title_id) REFERENCES movie_titles (id)"""
        cur.execute(add_fk_constraint_to_table)

        connection.commit()

        rejected_fk_rows = """SELECT * from alias_dump t1
                                LEFT JOIN movie_titles t2 ON t2.id = t1.title_id
                                WHERE t2.id IS NULL"""
        cur.execute(rejected_fk_rows)

        reject_logger = setup_logger(
            'reject_logger', './rejected_alias.log')
        for row in cur:
            reject_logger.info(row)
            reject_logger.error(
                "This tuple contains foreign key constraints error \n")
            rejected_count += 1

        exe_logger.info(
            "{0} rejected tuples: {1}".format(file, rejected_count))

    except (Exception, psycopg2.DatabaseError) as error:
        connection.rollback()
        print(error)


def insert_normalised_alias_from_dump():
    try:
        cur = connection.cursor()

        insert_region_from_staging_table = """INSERT into region(movie_id, ordering, region)
                                        SELECT a.title_id, a.ordering::int, region from alias_dump a
                                        inner join movie_titles m
                                        on m.id = a.title_id
                                        where a.region IS NOT NULL"""
        cur.execute(insert_region_from_staging_table)

        insert_attributes_from_staging_table = """INSERT into attributes(movie_id, ordering, attributes)
                                        SELECT a.title_id, a.ordering::int, unnest(string_to_array(attributes, ',')) from alias_dump a
                                        inner join movie_titles m
                                        on m.id = a.title_id
                                        where a.attributes IS NOT NULL"""
        cur.execute(insert_attributes_from_staging_table)

        insert_language_from_staging_table = """INSERT into language(movie_id, ordering, language)
                                        SELECT a.title_id, a.ordering::int, language from alias_dump a
                                        inner join movie_titles m
                                        on m.id = a.title_id
                                        where a.language IS NOT NULL """
        cur.execute(insert_language_from_staging_table)

        insert_movie_type_from_staging_table = """INSERT into movie_type(movie_id, ordering, type)
                                        SELECT a.title_id, a.ordering::int,unnest(string_to_array(a.types, ',')) from alias_dump a
                                        inner join movie_titles m
                                        on m.id = a.title_id
                                        where a.types IS NOT NULL"""
        cur.execute(insert_movie_type_from_staging_table)

        add_fk_constraint_to_region = """ALTER TABLE region
                                        ADD CONSTRAINT fk_movie_id
                                        FOREIGN KEY (movie_id) REFERENCES movie_titles (id)"""
        add_fk_constraint_to_attributes = """ALTER TABLE attributes
                                            ADD CONSTRAINT fk_movie_id
                                            FOREIGN KEY (movie_id) REFERENCES movie_titles (id)"""
        add_fk_constraint_to_language = """ALTER TABLE language
                                            ADD CONSTRAINT fk_movie_id
                                            FOREIGN KEY (movie_id) REFERENCES movie_titles (id)"""
        add_fk_constraint_to_movie_type = """ALTER TABLE movie_type
                                            ADD CONSTRAINT fk_movie_id
                                            FOREIGN KEY (movie_id) REFERENCES movie_titles (id)"""

        cur.execute(add_fk_constraint_to_region)
        cur.execute(add_fk_constraint_to_attributes)
        cur.execute(add_fk_constraint_to_language)
        cur.execute(add_fk_constraint_to_movie_type)

        connection.commit()

    except (Exception, psycopg2.DatabaseError) as error:
        connection.rollback()
        print(error)
#------------Insert alias------------------

#------------Insert names------------------


def insert_name_from_dump(file):
    rejected_count = 0
    try:
        cur = connection.cursor()
        staging_table = """CREATE TABLE names_dump (
                        id varchar ,
                        name varchar,
                        birth_year smallint,
                        death_year smallint,
                        profession varchar ,
                        movie_title varchar)"""
        cur.execute(staging_table)

        copy_data_to_staging = "COPY names_dump FROM '{0}' (FORMAT CSV, DELIMITER E'\t', " \
                               "NULL '\\N', HEADER true, QUOTE E'\b')".format(
                                   getURL(file + '/' + file))
        cur.execute(copy_data_to_staging)

        insert_from_staging_table = """INSERT into person
                                        SELECT d.id,d.name,d.birth_year,d.death_year
                                        from names_dump d"""
        cur.execute(insert_from_staging_table)

        connection.commit()

        exe_logger.info(
            "{0} rejected tuples: {1}".format(file, rejected_count))

    except (Exception, psycopg2.DatabaseError) as error:
        connection.rollback()
        print(error)


def insert_normalised_name_from_dump():
    try:
        cur = connection.cursor()

        insert_profession_from_staging_table = """INSERT into profession(id,profession)
                                        SELECT d.id, unnest(string_to_array(d.profession, ',')) from names_dump d
                                        inner join person p
                                        on p.id = d.id
                                        where d.profession != '' """
        cur.execute(insert_profession_from_staging_table)

        insert_actor_titles_from_staging_table = """INSERT into popular_actor_titles(person_id,movie_title)
                                        SELECT d.id, string_to_array(d.movie_title, ',') from names_dump d
                                        inner join person p
                                        on p.id = d.id
                                        where d.movie_title IS NOT NULL"""
        cur.execute(insert_actor_titles_from_staging_table)

        add_fk_constraint_to_profession = """ALTER TABLE profession
                                                ADD CONSTRAINT fk_id
                                                FOREIGN KEY (id) REFERENCES person (id)"""
        add_fk_constraint_to_actor_titles = """ALTER TABLE popular_actor_titles
                                        ADD CONSTRAINT fk_person_id
                                        FOREIGN KEY (person_id) REFERENCES person (id)"""

        cur.execute(add_fk_constraint_to_profession)
        cur.execute(add_fk_constraint_to_actor_titles)

        connection.commit()

    except (Exception, psycopg2.DatabaseError) as error:
        connection.rollback()
        print(error)
#---------Insert names----------------------

#------------Insert credits------------------


def insert_credits_from_dump(file):
    rejected_count = 0
    try:
        cur = connection.cursor()
        staging_table = """CREATE TABLE principals_dump (
            titleid varchar,
            ordering varchar,
            person_id varchar,
            category varchar,
            job varchar,
            characters varchar
        )"""
        cur.execute(staging_table)

        copy_data_to_staging = """COPY principals_dump FROM '{0}' (FORMAT CSV, DELIMITER E'\t',
                                NULL '\\N', HEADER true, QUOTE E'\b')""".format(getURL(file + '/' + file))
        cur.execute(copy_data_to_staging)

        insert_from_staging_table = """INSERT into credits(movie_title, ordering, person_id, category, job, character)
                                        SELECT a.titleid, a.ordering::int, a.person_id, a.category,
                                        a.job, REGEXP_REPLACE(a.characters,'\["|\"]','','g')
                                        from principals_dump a
                                        inner join movie_titles m
                                        on m.id = a.titleid
                                        inner join person p
                                        on a.person_id = p.id"""
        cur.execute(insert_from_staging_table)

        add_fk_constraint_to_table = """ALTER TABLE credits
                                        ADD CONSTRAINT fk_title_id
                                        FOREIGN KEY (movie_title) REFERENCES movie_titles (id)"""
        add_fk_constraint_person_to_table = """ALTER TABLE credits
                                                ADD CONSTRAINT fk_person_id
                                                FOREIGN KEY (person_id) REFERENCES person (id)"""
        cur.execute(add_fk_constraint_to_table)
        cur.execute(add_fk_constraint_person_to_table)

        connection.commit()

        rejected_fk_rows_movies = """SELECT * from principals_dump t1
                LEFT JOIN movie_titles t2 ON t2.id = t1.titleid
                WHERE t2.id IS NULL"""

        rejected_fk_rows_person = """SELECT * from principals_dump t1
                LEFT JOIN person t3 ON t3.id = t1.person_id
                WHERE t3.id is null"""

        cur.execute(rejected_fk_rows_movies)

        reject_logger = setup_logger(
            'reject_logger', './rejected_credits.log')
        for row in cur:
            rejected_count += 1
            reject_logger.info(row)
            reject_logger.error(
                "This tuple contains foreign key constraints error \n")

        cur.execute(rejected_fk_rows_person)
        for row in cur:
            rejected_count += 1
            reject_logger.info(row)
            reject_logger.error(
                "This tuple contains foreign key constraints error \n")

        exe_logger.info(
            "{0} rejected tuples: {1}".format(file, rejected_count))

    except (Exception, psycopg2.DatabaseError) as error:
        connection.rollback()
        print(error)
#------------Insert credits------------------


#-------------Insert movie details---------------
def insert_movie_plots(file):
    read_json = open(getURL(file), "r")
    data = []
    rejected_count = 0

    lines = read_json.readlines()
    for line in lines:
        json_object = json.loads(line)
        data.append(json_object)

    updatedData = []
    reject_logger = setup_logger(
        'reject_logger', './rejected_movdetails.log')

    for row in data:
        updatedrow = {}
        if ((row['plot_synopsis'] == "") | (row['plot_summary'] == "")):
            rejected_count += 1
            reject_logger.info(row)
            reject_logger.error("This tuple contains null values. \n")
        else:
            updatedrow["movie_id"] = row['movie_id']
            updatedrow["plot_synopsis"] = trim_spaces(row['plot_synopsis'])
            updatedrow["plot_summary"] = trim_spaces(row['plot_summary'])
            updatedData.append(updatedrow)

    cur = connection.cursor()
    savept = uuid.uuid1().hex

    for rec in updatedData:
        cur.execute('SAVEPOINT "%s"' % savept)
        try:
            cur.execute("""INSERT INTO movie_plots VALUES (%s,%s,%s)""",
                        (rec['movie_id'], rec['plot_synopsis'],
                         rec['plot_summary']))
        except (Exception, psycopg2.DatabaseError) as error:
            rejected_count += 1
            reject_logger.info(rec)
            reject_logger.error("{0} \n".format(error))
            print(error)
            cur.execute('ROLLBACK TO SAVEPOINT "%s"' % savept)

        else:
            cur.execute('RELEASE SAVEPOINT "%s"' % savept)

    connection.commit()


def insert_into_db(filename):

 # Big file- load one at a time

    if filename == 'title.basics.tsv':
        insert_title_from_dump(filename)
        insert_normalised_title_from_dump()

    if filename == 'title.akas.tsv':
        insert_alias_from_dump(filename)
        insert_normalised_alias_from_dump()

    if filename == 'name.basics.tsv':
        insert_name_from_dump(filename)
        insert_normalised_name_from_dump()

    if filename == 'title.principals.tsv':
        insert_credits_from_dump(filename)

    if filename == 'title.ratings.tsv':
        insert_rating_from_dump(filename)

    if filename == 'IMDB_movie_details.json':
        insert_movie_plots(filename)


if __name__ == "__main__":
    _start = time.time()
    # specify directory in getURL function
    # tables
    # Huge Files: 'title.akas.tsv','title.principals.tsv'
    #'title.basics.tsv','name.basics.tsv'
    # 'title.ratings.tsv','IMDB_movie_details.json'

    file = 'name.basics.tsv'

    exe_logger = setup_logger('exe_logger', './logfile.log')

    insert_into_db(file)

    print("took {0} seconds".format((time.time() - _start)))
    print(resource.getrusage(resource.RUSAGE_SELF).ru_maxrss)

    # Log time and memory
    exe_logger.info("{0} execution time:{1}".format(
        file, (time.time() - _start)))
    exe_logger.info("{0} memory usage: {1} \n".format(file,
                                                      resource.getrusage(resource.RUSAGE_SELF).ru_maxrss))
