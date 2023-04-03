from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask_swagger_ui import get_swaggerui_blueprint

from .weather_api import weather_api

from weather_api.api.swagger import setup_swagger

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///weather.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

db = SQLAlchemy(app)

app.register_blueprint(weather_api)

SWAGGER_URL = '/api/docs'
API_URL = '/api/spec'

swaggerui_blueprint = get_swaggerui_blueprint(
    SWAGGER_URL,
    API_URL,
    config={
        'app_name': 'Weather API'
    }
)

app.register_blueprint(swaggerui_blueprint, url_prefix=SWAGGER_URL)

setup_swagger(app)

if __name__ == '__main__':
    app.run()
