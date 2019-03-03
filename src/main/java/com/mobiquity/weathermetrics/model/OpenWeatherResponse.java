package com.mobiquity.weathermetrics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Parse REST response and map same into List of OpenWeatherModel.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class OpenWeatherResponse {
    private List<OpenWeatherModel> openWeatherForcastList;

    @SuppressWarnings(value = {"ünchecked", "PMD"})
    @JsonProperty("list")
    private void parseJson(List<Map<String, Object>> jsonList) {
        openWeatherForcastList = new ArrayList<>();

        log.trace("jsonList" + jsonList);

        try {
            jsonList.forEach(map -> {
                OpenWeatherModel openWeatherModel = new OpenWeatherModel();
                openWeatherModel.setDateTime(LocalDateTime.parse((String) map.get("dt_txt"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                if (map.get("main") != null) {
                    Map<String, Object> mainMap = (Map<String, Object>) map.get("main");

                    if (mainMap.get("temp") != null)
                        openWeatherModel.setTemp(((Number) mainMap.get("temp")).doubleValue());

                    if (mainMap.get("pressure") != null)
                        openWeatherModel.setPressure(((Number) mainMap.get("pressure")).doubleValue());
                }

                openWeatherForcastList.add(openWeatherModel);
            });
        } catch (Exception e) {
            log.error("Error while parsing response.", e);
            throw new IllegalStateException("Error while parsing response.", e);
        }

        log.trace("openWeatherForcastList = " + openWeatherForcastList);
    }

}
