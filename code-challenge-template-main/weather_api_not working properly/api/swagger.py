from flask import jsonify
from flasgger import Swagger


def setup_swagger(app):
    swagger_config = {
        "title": "Weather API",
        "specs_route": "/api/docs/",
        "swagger_ui": True,
        "openapi": "3.0.2",
        "uiversion": 3,
    }

    Swagger(app, config=swagger_config)

    @app.route("/api/spec")
    def spec():
        swag = app.config.get("SWAGGER")
        swag["info"]["version"] = "1.0"
        swag["basePath"] = "/"
        return jsonify(swag)
