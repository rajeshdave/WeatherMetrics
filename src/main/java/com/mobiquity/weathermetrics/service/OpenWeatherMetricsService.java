package com.mobiquity.weathermetrics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobiquity.weathermetrics.model.OpenWeatherModel;
import com.mobiquity.weathermetrics.model.OpenWeatherResponse;
import com.mobiquity.weathermetrics.model.WeatherMetrics;
import com.mobiquity.weathermetrics.rest.WeatherDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Make call to OpenWeather rest service through WeatherDataService.
 * Read response from WeatherDataService and build WeatherMetrics out of it.
 */
@Component
@Slf4j
public class OpenWeatherMetricsService implements WeatherMetricsService {

    @Autowired
    private WeatherDataService weatherDataService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Make call to OpenWeather rest service through WeatherDataService.
     * Read response from WeatherDataService and build WeatherMetrics out of it.
     *
     * @param cityName represent cityName for which we are interested in metrics
     * @return json response of WeatherMetrics which contains average day temperature, night temperature and pressure for next 3 days
     */
    @Override
    public String getWeatherMetrics(String cityName) {
        log.debug("Processing request for " + cityName);
        String dataJson = weatherDataService.getWeatherData(cityName);
        OpenWeatherResponse openWeatherResponse = mapOpenWeatherResponse(dataJson);
        WeatherMetrics weatherMetrics = createWeatherMetrics(openWeatherResponse);
        String weatherMetricsJson = writeWeatherMetrics(weatherMetrics);
        log.trace(">>> weatherMetricsJson" + weatherMetricsJson);
        return weatherMetricsJson;
    }

    private OpenWeatherResponse mapOpenWeatherResponse(String jsonResponse) {
        try {
            log.trace("Open weather Response = " + jsonResponse);
            return objectMapper.readValue(jsonResponse, OpenWeatherResponse.class);
        } catch (Exception e) {
            throw new IllegalStateException("Error while parsing json.", e);
        }
    }

    /**
     * Json response of forecast data contains data of every 3 hours. Here, we are preparing below metrics for 3 days.
     * 1. Average of daily (06:00 – 18:00) and nightly (18:00 – 06:00) temperatures in
     * Celsius for the next 3 days from today’s date.
     * 2. Average of pressure for the next 3 days from today’s date.
     *
     * We are passing 'cnt=24' to collect next 3 days records.
     *
     * @param openWeatherResponse represents forecast data
     * @return WeatherMetrics
     */
    private WeatherMetrics createWeatherMetrics(OpenWeatherResponse openWeatherResponse) {
        WeatherMetrics weatherMetrics = new WeatherMetrics();

        if (openWeatherResponse != null && openWeatherResponse.getOpenWeatherForcastList() != null) {
            Map<LocalDate, List<OpenWeatherModel>> dateWiseForecast = openWeatherResponse
                    .getOpenWeatherForcastList()
                    .stream()
                    .collect(Collectors.groupingBy(openWeatherModel -> openWeatherModel.getDateTime().toLocalDate()));

            dateWiseForecast.forEach((date, listOpenWeatherModel) -> {
                //We are passing 'cnt=24' to collect next 3 days records. In case, we are getting data of fourth day then we are just ignoring same.
                if (date.isAfter(LocalDate.now().plusDays(2)))
                    return;

                Map<String, Double> map = new HashMap<>();

                map.put(WeatherMetrics.PRESSURE_KEY, listOpenWeatherModel.stream().collect(Collectors.averagingDouble(OpenWeatherModel::getPressure)));

                Map<Boolean, List<OpenWeatherModel>> dayNightWiseList = listOpenWeatherModel.stream()
                        .collect(Collectors.partitioningBy(openWeatherModel -> isDayTime(openWeatherModel.getDateTime().toLocalTime())));

                Double dayAvgtemp = getAverageTemp(dayNightWiseList.get(true));
                if (dayAvgtemp != 0)
                    map.put(WeatherMetrics.DAY_TEMP_KEY, dayAvgtemp);

                Double nightAvgtemp = getAverageTemp(dayNightWiseList.get(false));
                if (nightAvgtemp != 0)
                    map.put(WeatherMetrics.NIGHT_TEMP_KEY, nightAvgtemp);

                weatherMetrics.getPerDayMetrics().put(date.toString(), map);
            });
        }

        return weatherMetrics;
    }

    private Double getAverageTemp(List<OpenWeatherModel> openWeatherModelList) {
        return openWeatherModelList.stream().collect(Collectors.averagingDouble(OpenWeatherModel::getTemp));
    }

    private boolean isDayTime(LocalTime localTime) {
        if ((localTime.equals(LocalTime.of(06, 00, 00)) || localTime.isAfter(LocalTime.of(06, 00, 00)))
                && localTime.isBefore(LocalTime.of(18, 00, 00)))
            return true;

        return false;
    }

    private String writeWeatherMetrics(WeatherMetrics weatherMetrics) {
        try {
            String weatherMetricsResponse = objectMapper.writeValueAsString(weatherMetrics);
            log.trace("Weather Metrics Response" + weatherMetricsResponse);
            return weatherMetricsResponse;
        } catch (Exception e) {
            throw new IllegalStateException("Error while writing WeatherMetrics.", e);
        }
    }
}
