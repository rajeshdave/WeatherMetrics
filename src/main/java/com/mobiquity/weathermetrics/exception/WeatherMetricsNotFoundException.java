package com.mobiquity.weathermetrics.exception;

import lombok.Getter;

/**
 * Exception handling for 404.
 * Further, ExceptionHandlerController is for other exception types.
 */
@Getter
public class WeatherMetricsNotFoundException extends RuntimeException {
    private final String id = "WEATHER_METRICS_E001";

    public WeatherMetricsNotFoundException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
