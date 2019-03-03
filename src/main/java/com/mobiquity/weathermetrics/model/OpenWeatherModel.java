package com.mobiquity.weathermetrics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenWeatherModel {
    Double temp;
    Double pressure;
    LocalDateTime dateTime;

}
