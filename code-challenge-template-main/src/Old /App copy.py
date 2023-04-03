from flask import Flask, request
from flask_restx import Api, Resource, fields
from flask_cors import CORS
import sqlite3

app = Flask(__name__)
CORS(app)

api = Api(
    app,
    title='Weather API',
    description='API for accessing weather data',
    doc='/docs'
)

weather_model = api.model('Weather', {
    'id': fields.Integer(),
    'station_id': fields.String(),
    'date': fields.String(),
    'max_temperature': fields.Float(),
    'min_temperature': fields.Float(),
    'precipitation': fields.Float()
})

stats_model = api.model('Stats', {
    'id': fields.Integer(),
    'year': fields.Integer(),
    'station_id': fields.String(),
    'avg_max_temp': fields.Float(),
    'avg_min_temp': fields.Float(),
    'total_precipitation': fields.Float()
})

@api.route('/api/weather')
class Weather(Resource):
    @api.marshal_list_with(weather_model)
    def get(self):
        conn = sqlite3.connect('weather.db')
        cursor = conn.cursor()
        
        # Parse query parameters
        date = request.args.get('date')
        station_id = request.args.get('station_id')
        limit = int(request.args.get('limit', 10))
        offset = int(request.args.get('offset', 0))
        
        # Build SQL query
        query = 'SELECT * FROM weather_data'
        if date:
            query += f' WHERE date = "{date}"'
        if station_id:
            query += f' AND station_id = "{station_id}"'
        #query += f' LIMIT {limit} OFFSET {offset}'
        
        # Execute query and return results
        results = cursor.execute(query).fetchall()
        return [dict(zip(('id', 'station_id', 'date', 'max_temperature', 'min_temperature', 'precipitation'), row)) for row in results]

@api.route('/api/weather/stats')
class Stats(Resource):
    @api.marshal_list_with(stats_model)
    def get(self):
        conn = sqlite3.connect('weather.db')
        cursor = conn.cursor()
        
        # Parse query parameters
        year = request.args.get('year')
        station_id = request.args.get('station_id')
        limit = int(request.args.get('limit', 10))
        offset = int(request.args.get('offset', 0))
        
        # Build SQL query
        query = 'SELECT * FROM weather_stats'
        if year:
            query += f' WHERE year = {year}'
        if station_id:
            query += f' AND station_id = "{station_id}"'
        #query += f' LIMIT {limit} OFFSET {offset}'
        
        # Execute query and return results
        results = cursor.execute(query).fetchall()
        return [dict(zip(('id', 'year', 'station_id', 'avg_max_temp', 'avg_min_temp', 'total_precipitation'), row)) for row in results]

if __name__ == '__main__':
    app.run(debug=True)
