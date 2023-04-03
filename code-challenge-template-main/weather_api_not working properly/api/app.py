from flask import Flask
from flask_restful import Api
from flask_sqlalchemy import SQLAlchemy
from flask_swagger_ui import get_swaggerui_blueprint

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///weather.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

api = Api(app)
db = SQLAlchemy(app)

SWAGGER_URL = '/swagger'
API_URL = '/swagger.json'

swagger_ui_blueprint = get_swaggerui_blueprint(
    SWAGGER_URL,
    API_URL,
    config={
        'app_name': 'Weather API'
    }
)

app.register_blueprint(swagger_ui_blueprint, url_prefix=SWAGGER_URL)

from resources.weather import WeatherDataResource
from resources.weather_stats import WeatherStatsResource

api.add_resource(WeatherDataResource, '/api/weather')
api.add_resource(WeatherStatsResource, '/api/weather/stats')

if __name__ == '__main__':
    app.run(debug=True)