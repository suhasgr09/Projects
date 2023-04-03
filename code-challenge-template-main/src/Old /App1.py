from flask import Flask, request
from flask_restful import Api, Resource, reqparse, fields, marshal_with
from flask_swagger_ui import get_swaggerui_blueprint
import sqlite3

app = Flask(__name__)
api = Api(app)

SWAGGER_URL = '/swagger'
API_URL = '/api/swagger.json'

swagger_ui_blueprint = get_swaggerui_blueprint(
    SWAGGER_URL,
    API_URL,
    config={
        'app_name': 'Weather API'
    }
)
app.register_blueprint(swagger_ui_blueprint, url_prefix=SWAGGER_URL)

DATABASE = 'weather.db'

# Define request parser for filtering and pagination
weather_parser = reqparse.RequestParser()
weather_parser.add_argument('start_date', type=str, help='Start date (YYYY-MM-DD)')
weather_parser.add_argument('end_date', type=str, help='End date (YYYY-MM-DD)')
weather_parser.add_argument('station_id', type=str, help='Station ID')
weather_parser.add_argument('page', type=int, default=1, help='Page number')
weather_parser.add_argument('page_size', type=int, default=10, help='Page size')


# Define resource for /api/weather endpoint
class WeatherResource(Resource):
    # Define response fields for marshalling
    weather_fields = {
        'id': fields.Integer,
        'station_id': fields.String,
        'date': fields.String,
        'max_temperature': fields.Float,
        'min_temperature': fields.Float,
        'precipitation': fields.Float
    }

    # Define GET method for /api/weather endpoint
    @marshal_with(weather_fields)
    def get(self):
        # Parse query string
        args = weather_parser.parse_args()
        start_date = args.get('start_date')
        end_date = args.get('end_date')
        station_id = args.get('station_id')
        page = args.get('page')
        page_size = args.get('page_size')

        # Calculate offset for pagination
        offset = (page - 1) * page_size

        # Build SQL query based on query string
        query = 'SELECT * FROM weather_data WHERE 1=1'
        if start_date:
            query += f" AND date >= '{start_date}'"
        if end_date:
            query += f" AND date <= '{end_date}'"
        if station_id:
            query += f" AND station_id = '{station_id}'"
        query += f" LIMIT {page_size} OFFSET {offset}"

        # Execute query and fetch results
        conn = sqlite3.connect(DATABASE)
        cursor = conn.execute(query)
        results = cursor.fetchall()
        conn.close()

        return results


# Define resource for /api/weather/stats endpoint
class WeatherStatsResource(Resource):
    # Define response fields for marshalling
    stats_fields = {
        'year': fields.Integer,
        'station_id': fields.String,
        'avg_max_temp': fields.Float,
        'avg_min_temp': fields.Float,
        'total_precipitation': fields.Float
    }

    # Define GET method for /api/weather/stats endpoint
    @marshal_with(stats_fields)
    def get(self):
        # Build SQL query to calculate weather stats
        query = '''
        SELECT 
            strftime('%Y', date) AS year, 
            station_id, 
            AVG(max_temperature) AS avg_max_temp, 
            AVG(min_temperature) AS avg_min_temp, 
            SUM(precipitation)'''
        
