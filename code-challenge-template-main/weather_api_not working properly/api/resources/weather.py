from flask_restful import Resource, reqparse, fields, marshal_with
from models import WeatherData
from app import db

weather_fields = {
    'id': fields.Integer,
    'station_id': fields.String,
    'date': fields.String,
    'max_temperature': fields.Float,
    'min_temperature': fields.Float,
    'precipitation': fields.Float
}

class WeatherDataResource(Resource):
    parser = reqparse.RequestParser()
    parser.add_argument('station_id', type=str, help='Station ID')
    parser.add_argument('start_date', type=str, help='Start date (YYYY-MM-DD)')
    parser.add_argument('end_date', type=str, help='End date (YYYY-MM-DD)')
    parser.add_argument('page', type=int, default=1, help='Page number')
    parser.add_argument('per_page', type=int, default=10, help='Items per page')

    @marshal_with(weather_fields)
    def get(self):
        args = self.parser.parse_args()
        station_id = args.get('station_id')
        start_date = args.get('start_date')
        end_date = args.get('end_date')
        page = args.get('page')
        per_page = args.get('per_page')
        
        query = WeatherData.query

        if station_id:
            query = query.filter(WeatherData.station_id == station_id)

        if start_date:
            query = query.filter(WeatherData.date >= start_date)

        if end_date:
            query = query.filter(WeatherData.date <= end_date)

        paginated_query = query.paginate(page=page, per_page=per_page)
        results = paginated_query.items

        return results
