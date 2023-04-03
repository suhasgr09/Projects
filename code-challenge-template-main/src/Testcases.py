import unittest
from datetime import date
from app import app, WeatherData, WeatherStats

class FlaskAppTests(unittest.TestCase):

    def setUp(self):
        self.app = app.test_client()
        self.app.testing = True

        # Insert test data into database
        data1 = WeatherData(station_id='ABC123', date=date(2022, 3, 1), max_temperature=20.0, min_temperature=10.0, precipitation=5.0)
        data2 = WeatherData(station_id='DEF456', date=date(2022, 3, 2), max_temperature=25.0, min_temperature=15.0, precipitation=2.5)
        data3 = WeatherData(station_id='ABC123', date=date(2022, 3, 3), max_temperature=22.0, min_temperature=12.0, precipitation=1.0)
        stats1 = WeatherStats(station_id='ABC123', year=2022, avg_max_temperature=21.0, avg_min_temperature=11.0, total_precipitation=6.0)
        stats2 = WeatherStats(station_id='DEF456', year=2022, avg_max_temperature=24.0, avg_min_temperature=14.0, total_precipitation=3.0)

        with app.app_context():
            db.session.add_all([data1, data2, data3, stats1, stats2])
            db.session.commit()

    def test_get_weather_data(self):
        response = self.app.get('/weather-data?page=1&per_page=2')
        self.assertEqual(response.status_code, 200)
        self.assertEqual(len(response.json), 2)
        self.assertEqual(response.headers['X-Total-Count'], '3')
        self.assertEqual(response.headers['X-Page'], '1')
        self.assertEqual(response.headers['X-Per-Page'], '2')

    def test_get_weather_data_filter_by_station_id(self):
        response = self.app.get('/weather-data?station_id=ABC123')
        self.assertEqual(response.status_code, 200)
        self.assertEqual(len(response.json), 2)

    def test_get_weather_data_filter_by_date(self):
        response = self.app.get('/weather-data?date=2022-03-02')
        self.assertEqual(response.status_code, 200)
        self.assertEqual(len(response.json), 1)
        self.assertEqual(response.json[0]['station_id'], 'DEF456')

    def test_get_weather_stats(self):
        response = self.app.get('/weather-stats?page=1&per_page=1')
        self.assertEqual(response.status_code, 200)
        self.assertEqual(len(response.json), 1)
        self.assertEqual(response.headers['X-Total-Count'], '2')
        self.assertEqual(response.headers['X-Page'], '1')
        self.assertEqual(response.headers['X-Per-Page'], '1')

    def test_get_weather_stats_filter_by_station_id(self):
        response = self.app.get('/weather-stats?station_id=DEF456')
        self.assertEqual(response.status_code, 200)
        self.assertEqual(len(response.json), 1)
        self.assertEqual(response.json[0]['year'], 2022)

    def test_get_weather_stats_filter_by_year(self):
        response = self.app.get('/weather-stats?year=2022')
        self.assertEqual(response.status_code, 200)
        self.assertEqual(len(response.json), 2)

if __name__ == '__main__':
    unittest.main()
