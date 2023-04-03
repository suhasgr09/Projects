from app import db

class WeatherData(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    station_id = db.Column(db.String(255), nullable=False)
    date = db.Column(db.Date, nullable=False)
    max_temperature = db.Column(db.Float, nullable=False)
    min_temperature = db.Column(db.Float, nullable=False)
    precipitation = db.Column(db.Float, nullable=False)

class WeatherStats(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    station_id = db.Column(db.String(255), nullable=False)
    year = db.Column(db.Integer, nullable=False)
    avg_max_temperature = db.Column(db.Float, nullable=False)
    avg_min_temperature = db.Column(db.Float, nullable=False)
    total_precipitation = db.Column(db.Float, nullable=False)
