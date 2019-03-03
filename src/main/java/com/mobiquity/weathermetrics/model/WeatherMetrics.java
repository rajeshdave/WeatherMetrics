package com.mobiquity.weathermetrics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherMetrics {
    public static final String PRESSURE_KEY = "Pressure";
    public static final String DAY_TEMP_KEY = "DayTemperature";
    public static final String NIGHT_TEMP_KEY = "NightTemperature";


    private Map<String, Map<String, Double>> perDayMetrics = new HashMap<>();

}
