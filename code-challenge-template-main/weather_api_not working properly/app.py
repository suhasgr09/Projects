from flask import Flask
from flask_restful import Api
from flask_sqlalchemy import SQLAlchemy
from config import DevelopmentConfig

app = Flask(__name__)
app.config.from_object(DevelopmentConfig)
api = Api(app)
db = SQLAlchemy(app)

from api.resources.weather import WeatherDataResource
from api.resources.weather_stats import WeatherStatsResource

api.add_resource(WeatherDataResource, '/api/weather')
api.add_resource(WeatherStatsResource, '/api/weather/stats')

if __name__ == '__main__':
    app.run()
