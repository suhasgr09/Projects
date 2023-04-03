# The code reads weather data from a directory of text files and inserts it into an SQLite database.
# Here's what the code does:
# The os and sqlite3 modules are imported to interact with the file system and the database, respectively. 
# The time module is also imported to measure the time it takes to ingest the data.
# DATA_DIR is a constant that specifies the path to the directory containing the weather data files.
# insert_weather_data() is a function that takes a cursor, a station ID, and a line of weather data as input.
# It parses the weather data from the line and inserts it into the weather_data table in the database, ignoring any duplicates.
# The code connects to the weather.db database and creates a cursor to execute SQL statements.
# The for loop iterates over each file in the DATA_DIR directory. 
# For each file, it extracts the station ID from the file name and opens the file. It then reads each line of the file and calls `insert_weather

# Import the required modules
import os
import sqlite3
import time

## Define the schema for the weather data records
#SCHEMA = '''
#CREATE TABLE IF NOT EXISTS weather_data (
#    id INTEGER PRIMARY KEY,
#    station_id TEXT,
#    date DATE,
#    max_temperature REAL,
#    min_temperature REAL,
#    precipitation REAL
#);
#'''

# Define the path to the directory containing the weather data files
DATA_DIR = 'wx_data/'

# Define a function to parse a single line of weather data and insert it into the database
def insert_weather_data(cur, station_id, line):
    # Split the line into fields using tab as the separator
    parts = line.strip().split('\t')
    # Extract the date from the first field
    date = parts[0]
    # Convert the maximum temperature from tenths of a degree Celsius to degrees Celsius
    max_temp = float(parts[1]) / 10
    # Convert the minimum temperature from tenths of a degree Celsius to degrees Celsius
    min_temp = float(parts[2]) / 10
    # Convert the precipitation from tenths of a millimeter to millimeters
    precipitation = float(parts[3]) / 10
    # Insert the data into the database, ignoring any duplicates
    cur.execute('INSERT OR IGNORE INTO weather_data (station_id, date, max_temperature, min_temperature, precipitation) VALUES (?, ?, ?, ?, ?)', (station_id, date, max_temp, min_temp, precipitation))

# Connect to the database and create the schema
conn = sqlite3.connect('weather.db')
cur = conn.cursor()
#cur.execute(SCHEMA)

# Ingest the weather data for each file in the data directory
start_time = time.time()
num_records = 0
for filename in os.listdir(DATA_DIR):
    # Check if the file is a text file
    if filename.endswith('.txt'):
        # Extract the station ID from the file name
        station_id = filename.split('.')[0]
        # Open the file and read each line
        with open(os.path.join(DATA_DIR, filename)) as f:
            for line in f:
                # Parse the weather data from the line and insert it into the database
                insert_weather_data(cur, station_id, line)
                num_records += 1

# Commit the changes and close the connection
conn.commit()
cur.close()
conn.close()

# Log the ingestion statistics
end_time = time.time()
print(f'Finished ingesting {num_records} weather data records in {end_time - start_time:.2f} seconds')
