package com.mobiquity.weathermetrics.rest;

/**
 * Make call to OpenWeather rest service
 */
public interface WeatherDataService {

    /**
     * Make call to OpenWeather rest service for given cityName.     *

     *
     * @param cityName represent cityName for which we are interested in metrics
     * @return json response from REST open weather endpoint
     */
    String getWeatherData(String cityName);
}
