package com.mobiquity.weathermetrics;

import com.mobiquity.weathermetrics.service.WeatherMetricsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OpenWeatherController {

    @Autowired
    private WeatherMetricsService weatherMetricsService;

    @GetMapping(name = "/weather")
    public String getWeatherMetrics(@RequestParam(value = "city") String city) {
        log.info("Processing request for " + city);

        if (city == null || city.isEmpty()) {
            throw new IllegalArgumentException("400 - Bad request. Request param city is mandatory.");
        }

        return weatherMetricsService.getWeatherMetrics(city);
    }
}
