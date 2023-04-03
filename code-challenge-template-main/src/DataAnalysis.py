import sqlite3

# Connect to the database
conn = sqlite3.connect('weather.db')
cur = conn.cursor()

# Create the weather_stats table if it doesn't already exist
#cur.execute('''
#CREATE TABLE IF NOT EXISTS weather_stats (
#    id INTEGER PRIMARY KEY,
#    station_id TEXT,
#    year INTEGER,
#    avg_max_temperature REAL,
#    avg_min_temperature REAL,
#    total_precipitation REAL
#);
#''')

# Get the list of all station IDs
cur.execute('SELECT DISTINCT station_id FROM weather_data')
stations = [row[0] for row in cur.fetchall()]

# Calculate the statistics for each station and year
for station_id in stations:
    # Get the list of all years for which data is available for this station
    cur.execute('SELECT DISTINCT substr(date, 1, 4) FROM weather_data WHERE station_id = ? ORDER BY substr(date, 1, 4) ASC', (station_id,))
    years = [int(row[0]) for row in cur.fetchall()]

    # Calculate the statistics for each year
    for year in years:
        # Calculate the average maximum temperature for this station and year
        cur.execute('SELECT AVG(max_temperature) FROM weather_data WHERE station_id = ? AND substr(date, 1, 4) = ?', (station_id, str(year)))
        avg_max_temp = cur.fetchone()[0]

        # Calculate the average minimum temperature for this station and year
        cur.execute('SELECT AVG(min_temperature) FROM weather_data WHERE station_id = ? AND substr(date, 1, 4) = ?', (station_id, str(year)))
        avg_min_temp = cur.fetchone()[0]

        # Calculate the total accumulated precipitation for this station and year
        cur.execute('SELECT SUM(precipitation) FROM weather_data WHERE station_id = ? AND substr(date, 1, 4) = ?', (station_id, str(year)))
        total_precip = cur.fetchone()[0]

        # Insert the statistics into the weather_stats table
        cur.execute('INSERT INTO weather_stats (station_id, year, avg_max_temperature, avg_min_temperature, total_precipitation) VALUES (?, ?, ?, ?, ?)', (station_id, year, avg_max_temp, avg_min_temp, total_precip))

# Commit the changes and close the connection
conn.commit()
cur.close()
conn.close()
