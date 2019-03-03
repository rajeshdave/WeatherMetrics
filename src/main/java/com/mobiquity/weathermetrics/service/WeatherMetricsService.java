package com.mobiquity.weathermetrics.service;

public interface WeatherMetricsService {

    /**
     * Make call to OpenWeather rest service through WeatherDataService.
     * Read response from WeatherDataService and build WeatherMetrics out of it.
     *
     * @param cityName represent cityName for which we are interested in metrics
     * @return json response of WeatherMetrics which contains average day temperature, night temperature and pressure for next 3 days
     */
    String getWeatherMetrics(String cityName);
}
