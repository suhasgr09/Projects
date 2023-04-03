from flask_restful import Resource, reqparse, fields, marshal_with
from models import WeatherStats
from app import db

weather_stats_fields = {
    'id': fields.Integer,
    'station_id': fields.String,
    'year': fields.Integer,
    'avg_max_temperature': fields.Float,
    'avg_min_temperature': fields.Float,
    'total_precipitation': fields.Float
}

class WeatherStatsResource(Resource):
    parser = reqparse.RequestParser()
    parser.add_argument('station_id', type=str, help='Station ID')
    parser.add_argument('year', type=int, help='Year')
    parser.add_argument('page', type=int, default=1, help='Page number')
    parser.add_argument('per_page', type=int, default=10, help='Items per page')

    @marshal_with(weather_stats_fields)
    def get(self):
        args = self.parser.parse_args()
        station_id = args.get('station_id')
        year = args.get('year')
        page = args.get('page')
        per_page = args.get('per_page')
        
        query = WeatherStats.query

        if station_id:
            query = query.filter(WeatherStats.station_id == station_id)

        if year:
            query = query.filter(WeatherStats.year == year)

        paginated_query = query.paginate(page=page, per_page=per_page)
        results = paginated_query.items
        return results
