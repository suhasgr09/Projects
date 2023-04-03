from flask import Flask, request
from flask_restful import Api, Resource, fields, marshal_with
from flask_restful_swagger import swagger
from sqlalchemy import create_engine, Column, Integer, String, Date, Float
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

DATABASE = 'weather.db'

app = Flask(__name__)
api = Api(app)

engine = create_engine(f'sqlite:///{DATABASE}')
Session = sessionmaker(bind=engine)
Base = declarative_base()


# The code imports necessary libraries and creates a Flask app instance named app, a Flask RESTful API instance named api,
# and a SQLAlchemy engine instance named engine to connect to the weather.db database.
# It also creates a Session instance to work with the database and a SQLAlchemy declarative base Base instance to define database tables as classes.

class WeatherData(Base):
    __tablename__ = 'weather_data'
    id = Column(Integer, primary_key=True)
    station_id = Column(String)
    date = Column(Date)
    max_temperature = Column(Float)
    min_temperature = Column(Float)
    precipitation = Column(Float)

class WeatherStats(Base):
    __tablename__ = 'weather_stats'
    id = Column(Integer, primary_key=True)
    station_id = Column(String)
    year = Column(Integer)
    avg_max_temperature = Column(Float)
    avg_min_temperature = Column(Float)
    total_precipitation = Column(Float)

Base.metadata.create_all(engine)

# The code defines two database tables as SQLAlchemy classes: 
# WeatherData and WeatherStats. Each class has its own attributes corresponding to the columns in the corresponding database table. 
# The Base.metadata.create_all(engine) command creates the database tables if they do not exist.

class WeatherDataResource(Resource):
    resource_fields = {
        'id': fields.Integer,
        'station_id': fields.String,
        'date': fields.String,
        'max_temperature': fields.Float,
        'min_temperature': fields.Float,
        'precipitation': fields.Float,
    }

    @swagger.operation(
        notes='Get weather data',
        parameters=[
            {
                'name': 'station_id',
                'description': 'Filter by station ID',
                'in': 'query',
                'type': 'string'
            },
            {
                'name': 'date',
                'description': 'Filter by date in format YYYY-MM-DD',
                'in': 'query',
                'type': 'string'
            },
            {
                'name': 'page',
                'description': 'Page number',
                'in': 'query',
                'type': 'integer'
            },
            {
                'name': 'per_page',
                'description': 'Number of items per page',
                'in': 'query',
                'type': 'integer'
            },
        ]
    )
    @marshal_with(resource_fields)
    def get(self):
        session = Session()

        station_id = request.args.get('station_id')
        date = request.args.get('date')
        page = int(request.args.get('page', 1))
        per_page = int(request.args.get('per_page', 10))

        query = session.query(WeatherData)

        if station_id:
            query = query.filter(WeatherData.station_id == station_id)

        if date:
            query = query.filter(WeatherData.date == date)

        total_count = query.count()

        data = query \
            .order_by(WeatherData.date.asc()) \
            .limit(per_page) \
            .offset((page - 1) * per_page) \
            .all()

        session.close()

        return data, {
            'X-Total-Count': total_count,
            'X-Page': page,
            'X-Per-Page': per_page
        }

class WeatherStatsResource(Resource):
    resource_fields = {
        'id': fields.Integer,
        'station_id': fields.String,
        'year': fields.Integer,
        'avg_max_temperature': fields.Float,
        'avg_min_temperature': fields.Float,
        'total_precipitation': fields.Float,
    }

    @swagger.operation(
        notes='Get weather stats',
        parameters=[
            {
                'name': 'station_id',
                'description': 'Filter by station ID',
                'in': 'query',
                'type': 'string'
            },
            {
                'name': 'year',
                'description': 'Filter by year',
                'in': 'query',
                'type': 'integer'
            },
            {
                'name': 'page',
                'description': 'Page number',
                'in': 'query',
                'type': 'integer'
            },
            {
                'name': 'per_page',
                'description': 'Number of items per page',
                'in': 'query',
                'type': 'integer'
            },
        ]
    )
    @marshal_with(resource_fields)
    def get(self):
        session = Session()

        station_id = request.args.get('station_id')
        year = request.args.get('year')
        page = int(request.args.get('page', 1))
        per_page = int(request.args.get('per_page', 10))

        query = session.query(WeatherStats)

        if station_id:
            query = query.filter(WeatherStats.station_id == station_id)

        if year:
            query = query.filter(WeatherStats.year == year)

        total_count = query.count()

        data = query \
            .order_by(WeatherStats.year.asc()) \
            .limit(per_page) \
            .offset((page - 1) * per_page) \
            .all()

        session.close()

        return data, {
            'X-Total-Count': total_count,
            'X-Page': page,
            'X-Per-Page': per_page
        }

api.add_resource(WeatherDataResource, '/api/weather')
api.add_resource(WeatherStatsResource, '/api/weather/stats')

if __name__ == '__main__':
    app.run(debug=True)
