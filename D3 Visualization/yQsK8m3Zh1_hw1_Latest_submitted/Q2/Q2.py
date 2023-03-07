########################### DO NOT MODIFY THIS SECTION ##########################
#################################################################################
import sqlite3
from sqlite3 import Error
import csv
#################################################################################

## Change to False to disable Sample
SHOW = True

############### SAMPLE CLASS AND SQL QUERY ###########################
######################################################################
class Sample():
    def sample(self):
        try:
            connection = sqlite3.connect("sample")
            connection.text_factory = str
        except Error as e:
            print("Error occurred: " + str(e))
        print('\033[32m' + "Sample: " + '\033[m')
        
        # Sample Drop table
        connection.execute("DROP TABLE IF EXISTS sample;")
        # Sample Create
        connection.execute("CREATE TABLE sample(id integer, name text);")
        # Sample Insert
        connection.execute("INSERT INTO sample VALUES (?,?)",("1","test_name"))
        connection.commit()
        # Sample Select
        cursor = connection.execute("SELECT * FROM sample;")
        print(cursor.fetchall())

######################################################################

class HW2_sql():
    ############### DO NOT MODIFY THIS SECTION ###########################
    ######################################################################
    def create_connection(self, path):
        connection = None
        try:
            connection = sqlite3.connect(path)
            connection.text_factory = str
        except Error as e:
            print("Error occurred: " + str(e))
    
        return connection

    def execute_query(self, connection, query):
        cursor = connection.cursor()
        try:
            if query == "":
                return "Query Blank"
            else:
                cursor.execute(query)
                connection.commit()
                return "Query executed successfully"
        except Error as e:
            return "Error occurred: " + str(e)
    ######################################################################
    ######################################################################

  
    
    # Part a.i Create Tables [2 points]
    def part_ai_1(self,connection):
        ############### EDIT SQL STATEMENT ###################################
        part_ai_1_sql = "CREATE TABLE movies(id INTEGER,title TEXT,score REAL);"
        ######################################################################
        
        return self.execute_query(connection, part_ai_1_sql)

    def part_ai_2(self,connection):
        ############### EDIT SQL STATEMENT ###################################
        part_ai_2_sql = "CREATE TABLE movie_cast(movie_id INTEGER,cast_id INTEGER,cast_name TEXT,birthday TEXT,popularity REAL);"
        ######################################################################
        
        return self.execute_query(connection, part_ai_2_sql)
    
    # Part a.ii Import Data [2 points]
    def part_aii_1(self,connection,path):
        ############### CREATE IMPORT CODE BELOW ############################
        with open(path) as csvfile:
            movies = csv.reader(csvfile, delimiter=',')
            for movie in movies:
                connection.execute('INSERT INTO movies VALUES (?,?,?)',(movie[0],movie[1], movie[2]))
       
       ######################################################################
        
        sql = "SELECT COUNT(id) FROM movies;"
        cursor = connection.execute(sql)
        return cursor.fetchall()[0][0]
    
    def part_aii_2(self,connection, path):
        ############### CREATE IMPORT CODE BELOW ############################
        with open(path) as csvfile:
            movie_cast = csv.reader(csvfile, delimiter=',')
            for i in movie_cast:
                connection.execute('INSERT INTO movie_cast VALUES (?,?,?,?,?)',(i[0], i[1], i[2], i[3], i[4]))
        
        
        ######################################################################
        
        sql = "SELECT COUNT(cast_id) FROM movie_cast;"
        cursor = connection.execute(sql)
        return cursor.fetchall()[0][0]

    # Part a.iii Vertical Database Partitioning [5 points]
    def part_aiii(self,connection):
        ############### EDIT CREATE TABLE SQL STATEMENT ###################################
        part_aiii_sql = "CREATE TABLE cast_bio (cast_id INTEGER,cast_name TEXT,birthday TEXT,popularity REAL);"
        ######################################################################
        
        self.execute_query(connection, part_aiii_sql)
        
        ############### CREATE IMPORT CODE BELOW ############################
        part_aiii_insert_sql = "INSERT INTO cast_bio (cast_id, cast_name, birthday, popularity) SELECT DISTINCT cast_id,cast_name,birthday,popularity from movie_cast;"
        ######################################################################
        
        self.execute_query(connection, part_aiii_insert_sql)
        
        sql = "SELECT COUNT(cast_id) FROM cast_bio;"
        cursor = connection.execute(sql)
        return cursor.fetchall()[0][0]
       

    # Part b Create Indexes [1 points]
    def part_b_1(self,connection):
        ############### EDIT SQL STATEMENT ###################################
        part_b_1_sql = "CREATE INDEX IF NOT EXISTS movie_index ON movies(id);"
        ######################################################################
        return self.execute_query(connection, part_b_1_sql)
    
    def part_b_2(self,connection):
        ############### EDIT SQL STATEMENT ###################################
        part_b_2_sql = "CREATE INDEX IF NOT EXISTS cast_index ON movie_cast(cast_id);"
        ######################################################################
        return self.execute_query(connection, part_b_2_sql)
    
    def part_b_3(self,connection):
        ############### EDIT SQL STATEMENT ###################################
        part_b_3_sql = "CREATE INDEX IF NOT EXISTS cast_bio_index ON cast_bio(cast_id);"
        ######################################################################
        return self.execute_query(connection, part_b_3_sql)
    
    # Part c Calculate a Proportion [3 points]
    def part_c(self,connection):
        ############### EDIT SQL STATEMENT ###################################
        #part_c_sql = 'WITH actors_1965_1985 AS (SELECT cast_id, cast_name, birthday, popularity FROM cast_bio WHERE (SUBSTR(birthday,-2) BETWEEN "65" AND "85") OR birthday IS "None") SELECT printf("%.2f", (100.0 * COUNT(actors_1965_1985.cast_id)) / (SELECT COUNT(*) FROM cast_bio)) AS proportion FROM actors_1965_1985;'
        part_c_sql = '''WITH actors_1965_1985 AS ( SELECT cast_id, cast_name, birthday, popularity FROM cast_bio WHERE 
        CAST(SUBSTR(birthday, -2) AS INTEGER) BETWEEN 65 AND 85) SELECT printf("%.2f", 
        (100.0 * COUNT(actors_1965_1985.cast_id)) / (SELECT COUNT(*) FROM cast_bio)) AS proportion FROM actors_1965_1985;'''
        ######################################################################
        cursor = connection.execute(part_c_sql)
        return cursor.fetchall()[0][0]

    # Part d Find the Most Prolific Actors [4 points]
    def part_d(self,connection):
        ############### EDIT SQL STATEMENT ###################################
        part_d_sql = '''SELECT cast_name, COUNT(movie_id) AS appearance_count FROM movie_cast
         WHERE popularity > 10 GROUP BY cast_id ORDER BY appearance_count DESC, cast_name ASC LIMIT 5;'''
        ######################################################################
        cursor = connection.execute(part_d_sql)
        return cursor.fetchall()

    # Part e Find the Highest Scoring Movies With the Least Amount of Cast [4 points]
    def part_e(self,connection):
        ############### EDIT SQL STATEMENT ###################################
        part_e_sql = '''WITH movie_cast_count AS ( SELECT movie_id, COUNT(*) AS cast_count FROM movie_cast 
        GROUP BY movie_id ), movie_score AS 
        ( SELECT id AS movie_id, title AS movie_title, score, cast_count FROM movies 
        JOIN movie_cast_count ON movies.id = movie_cast_count.movie_id ) 
        SELECT movie_title, printf("%.2f", score) AS movie_score, cast_count FROM movie_score 
        ORDER BY score DESC, cast_count ASC, movie_title LIMIT 5;'''
        ######################################################################
        cursor = connection.execute(part_e_sql)
        return cursor.fetchall()
    
    # Part f Get High Scoring Actors [4 points]
    def part_f(self,connection):
        ############### EDIT SQL STATEMENT ###################################
        part_f_sql = 'WITH actors_high_scoring_movies AS ( SELECT cast_id, AVG(score) AS avg_score FROM movie_cast JOIN movies ON movie_cast.movie_id = movies.id WHERE score >= 25 GROUP BY cast_id HAVING COUNT(*) >= 3 ) SELECT cast_bio.cast_id, cast_name, printf("%.2f", avg_score) AS average_score FROM actors_high_scoring_movies JOIN cast_bio ON actors_high_scoring_movies.cast_id = cast_bio.cast_id ORDER BY avg_score DESC, cast_name ASC LIMIT 10;'
        ######################################################################
        cursor = connection.execute(part_f_sql)
        return cursor.fetchall()

    # Part g Creating Views [6 points]
    def part_g(self,connection):
        ############### EDIT SQL STATEMENT ###################################
        part_g_sql = '''CREATE VIEW good_collaboration
                        AS
                        SELECT
                        MIN(cast_id1, cast_id2) AS cast_member_id1,
                        MAX(cast_id1, cast_id2) AS cast_member_id2,
                        COUNT(movie_id) AS movie_count,
                        AVG(score) AS average_movie_score
                        FROM (
                        SELECT
                        movie_cast.cast_id AS cast_id1,
                        DMC.cast_id AS cast_id2,
                        movie_cast.movie_id,
                        movies.score
                        FROM movie_cast 
                        JOIN movie_cast DMC ON movie_cast.movie_id = DMC.movie_id
                        JOIN movies  ON movie_cast.movie_id = movies.id
                        WHERE movie_cast.cast_id < DMC.cast_id
                        )
                        GROUP BY cast_member_id1, cast_member_id2
                        HAVING movie_count >= 3 AND average_movie_score >= 40;'''


        
        ######################################################################
        return self.execute_query(connection, part_g_sql)
    
    def part_gi(self,connection):
        ############### EDIT SQL STATEMENT ###################################
        
        part_g_i_sql =   '''
                            SELECT cast_bio.cast_id,cast_bio.cast_name,printf("%.2f", AVG(good_view.average_movie_score)) AS collaboration_score FROM 
                           (SELECT cast_member_id1 AS cast_id, average_movie_score
                        FROM good_collaboration
                        UNION ALL
                        SELECT cast_member_id2 AS cast_id, average_movie_score
                        FROM good_collaboration)
                            good_view
                            INNER JOIN  
                            cast_bio 
                            ON  good_view.cast_id = cast_bio.cast_id 
                            GROUP BY good_view.cast_id
                            ORDER BY AVG(good_view.average_movie_score) DESC, cast_bio.cast_name ASC LIMIT 5;
                              '''
        

        ######################################################################
       # cursor = connection.execute(part_g_i_view)
        cursor = connection.execute(part_g_i_sql)
        return cursor.fetchall()
    
    # Part h FTS [4 points]
    def part_h(self,connection,path):
        ############### EDIT SQL STATEMENT ###################################
        part_h_sql = "CREATE VIRTUAL TABLE movie_overview USING fts4(id INTEGER, overview TEXT);"
        ######################################################################
        connection.execute(part_h_sql)
        ############### CREATE IMPORT CODE BELOW ############################
        with open(path,encoding='utf-8-sig') as file:
            movies = csv.reader(file, delimiter=',')
            for movie in movies:
                connection.execute('INSERT INTO movie_overview(id, overview) VALUES (?,?)', (int(movie[0]), movie[1]))

        ######################################################################
        sql = "SELECT COUNT(id) FROM movie_overview;"
        cursor = connection.execute(sql)
        return cursor.fetchall()[0][0]
        
    def part_hi(self,connection):
        ############### EDIT SQL STATEMENT ###################################
        part_hi_sql = "SELECT count(*) FROM movie_overview WHERE overview MATCH 'fight';"
        ######################################################################
        cursor = connection.execute(part_hi_sql)
        return cursor.fetchall()[0][0]
    
    def part_hii(self,connection):
        ############### EDIT SQL STATEMENT ###################################
        part_hii_sql = 'SELECT COUNT(*) FROM movie_overview WHERE overview MATCH "space NEAR/5 program";'
        ######################################################################
        cursor = connection.execute(part_hii_sql)
        return cursor.fetchall()[0][0]


if __name__ == "__main__":
    
    ########################### DO NOT MODIFY THIS SECTION ##########################
    #################################################################################
    if SHOW == True:
        sample = Sample()
        sample.sample()

    print('\033[32m' + "Q2 Output: " + '\033[m')
    db = HW2_sql()
    try:
        conn = db.create_connection("Q2")
    except:
        print("Database Creation Error")

    try:
        conn.execute("DROP TABLE IF EXISTS movies;")
        conn.execute("DROP TABLE IF EXISTS movie_cast;")
        conn.execute("DROP TABLE IF EXISTS cast_bio;")
        conn.execute("DROP VIEW IF EXISTS good_collaboration;")
        conn.execute("DROP TABLE IF EXISTS movie_overview;")
    except:
        print("Error in Table Drops")

    try:
        print('\033[32m' + "part ai 1: " + '\033[m' + str(db.part_ai_1(conn)))
        print('\033[32m' + "part ai 2: " + '\033[m' + str(db.part_ai_2(conn)))
    except:
         print("Error in Part a.i")

    try:
        print('\033[32m' + "Row count for Movies Table: " + '\033[m' + str(db.part_aii_1(conn,"data/movies.csv")))
        print('\033[32m' + "Row count for Movie Cast Table: " + '\033[m' + str(db.part_aii_2(conn,"data/movie_cast.csv")))
    except:
        print("Error in part a.ii")

    try:
        print('\033[32m' + "Row count for Cast Bio Table: " + '\033[m' + str(db.part_aiii(conn)))
    except:
        print("Error in part a.iii")

    try:
        print('\033[32m' + "part b 1: " + '\033[m' + db.part_b_1(conn))
        print('\033[32m' + "part b 2: " + '\033[m' + db.part_b_2(conn))
        print('\033[32m' + "part b 3: " + '\033[m' + db.part_b_3(conn))
    except:
        print("Error in part b")

    try:
        print('\033[32m' + "part c: " + '\033[m' + str(db.part_c(conn)))

    except:
        print("Error in part c")

    try:
        print('\033[32m' + "part d: " + '\033[m')
        for line in db.part_d(conn):
            print(line[0],line[1])
    
    except:
        print("Error in part d")

    try:
        print('\033[32m' + "part e: " + '\033[m')
        for line in db.part_e(conn):
            print(line[0],line[1],line[2])
    except:
        print("Error in part e")

    try:
        print('\033[32m' + "part f: " + '\033[m')
        for line in db.part_f(conn):
            print(line[0],line[1],line[2])
    except:
        print("Error in part f")
    
    try:
        print('\033[32m' + "part g: " + '\033[m' + str(db.part_g(conn)))
        print('\033[32m' + "part g.i: " + '\033[m')
        for line in db.part_gi(conn):
            print(line[0],line[1],line[2])
    except Exception as e:
    # Code to be executed in case of a different exception
         print("Another error occurred:", e)
    #except:
    #   print("Error in part g")

    try:   
        print('\033[32m' + "part h.i: " + '\033[m'+ str(db.part_h(conn,"data/movie_overview.csv")))
        print('\033[32m' + "Count h.ii: " + '\033[m' + str(db.part_hi(conn)))
        print('\033[32m' + "Count h.iii: " + '\033[m' + str(db.part_hii(conn)))
    except:
        print("Error in part h")

    conn.close()
    #################################################################################
    #################################################################################
  
