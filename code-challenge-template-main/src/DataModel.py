#The sqlite3 module is used to interact with SQLite databases. 
#The DATABASE variable specifies the name of the SQLite database file that will be created.
#SCHEMA and SCHEMA2 are SQL schemas for the two tables that will be created in the database. 
#The first table is called weather_data and contains columns for id, station_id, date, max_temperature, min_temperature, and precipitation.
#The second table is called weather_stats and contains columns for id, station_id, year, avg_max_temperature, avg_min_temperature, and total_precipitation.
#The create_database function connects to the database specified by DATABASE, creates the two tables by executing the SQL statements in SCHEMA and SCHEMA2, 
#commits the changes, and closes the connection.
#Finally, if this script is being run directly (i.e., not imported as a module), the create_database function is called.

# Import the sqlite3 module
import sqlite3

# Define the name of the database file
DATABASE = 'weather.db'

# Define the SQL schema for the first table
SCHEMA = '''
CREATE TABLE IF NOT EXISTS weather_data (
    id INTEGER PRIMARY KEY,
    station_id TEXT,
    date DATE,
    max_temperature REAL,
    min_temperature REAL,
    precipitation REAL
);
'''

# Define the SQL schema for the second table
SCHEMA2 = '''
CREATE TABLE IF NOT EXISTS weather_stats (
    id INTEGER PRIMARY KEY,
    station_id TEXT,
    year INTEGER,
    avg_max_temperature REAL,
    avg_min_temperature REAL,
    total_precipitation REAL
);
'''

# Define a function to create the database and tables
def create_database():
    # Connect to the database
    conn = sqlite3.connect(DATABASE)
    # Create the first table
    conn.execute(SCHEMA)
    # Create the second table
    conn.execute(SCHEMA2)
    # Commit the changes
    conn.commit()
    # Close the connection
    conn.close()

# If this script is being run directly, call the create_database function
if __name__ == '__main__':
    create_database()

#The sqlite3 module is used to interact with SQLite databases. The DATABASE variable specifies the name of the SQLite database file that will be created.

#SCHEMA and SCHEMA2 are SQL schemas for the two tables that will be created in the database. The first table is called weather_data and contains columns for id, station_id, date, max_temperature, min_temperature, and precipitation. The second table is called weather_stats and contains columns for id, station_id, year, avg_max_temperature, avg_min_temperature, and total_precipitation.

#The create_database function connects to the database specified by DATABASE, creates the two tables by executing the SQL statements in SCHEMA and SCHEMA2, commits the changes, and closes the connection.

#Finally, if this script is being run directly (i.e., not imported as a module), the create_database function is called.