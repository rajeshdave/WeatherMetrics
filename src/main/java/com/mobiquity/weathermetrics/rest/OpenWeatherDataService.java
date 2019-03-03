package com.mobiquity.weathermetrics.rest;

import com.mobiquity.weathermetrics.exception.WeatherMetricsNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Make call to OpenWeather rest service
 */
@Component
@Slf4j
public class OpenWeatherDataService implements WeatherDataService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${endpoint.openweather.uri}")
    private String endpointUri;

    @Value("${endpoint.openweather.appid}")
    private String endpointAppId;

    @Value("${endpoint.openweather.cnt}")
    private String endpointCnt;

    /**
     * Make call to OpenWeather rest service for given cityName.
     *
     * We will be hitting below URL, which is returning data for every 3 hours.
     *    http://api.openweathermap.org/data/2.5/forecast?q=London,uk&units=metric&cnt=24&APPID=4d8d9da673e70c41ef740081db5e4a10
     *
     *    We are passing 'cnt=24' to collect next 3 days records. In case, we are getting data of fourth day then we are just ignoring same.
     *
     * @param cityName represent cityName for which we are interested in metrics
     * @return json response from REST open weather endpoint
     */
    @Override
    public String getWeatherData(String cityName) {
        try {
            log.debug("Processing request for " + cityName);
            return restTemplate.getForObject(String.format("%s&APPID=%s&cnt=%s&q=%s", endpointUri, endpointAppId, endpointCnt, cityName), String.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new WeatherMetricsNotFoundException("404 Not found. Weather data not found for " + cityName);
        }
    }
}
