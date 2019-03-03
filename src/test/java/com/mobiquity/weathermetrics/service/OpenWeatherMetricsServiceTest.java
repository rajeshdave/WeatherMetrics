package com.mobiquity.weathermetrics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobiquity.weathermetrics.WeatherMetricsApplicationTests;
import com.mobiquity.weathermetrics.model.WeatherMetrics;
import com.mobiquity.weathermetrics.rest.WeatherDataService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Testing logic of WeatherMetricsService without making actual REST call.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OpenWeatherMetricsServiceTest {

    @Autowired
    private WeatherMetricsService weatherMetricsService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WeatherDataService weatherDataService;

    @Before
    public void before() {
        String content = readResourse("/response.json");
        Mockito.when(weatherDataService.getWeatherData(Mockito.anyString())).thenReturn(content);
    }

    @Test
    public void testAverageMetrics() throws IOException {
        String metrics = weatherMetricsService.getWeatherMetrics("London,uk");
        WeatherMetrics weatherMetrics = objectMapper.readValue(metrics, WeatherMetrics.class);

        Map<String, Double> map = weatherMetrics.getPerDayMetrics().get("2019-03-01");
        Assertions.assertThat(map.get(WeatherMetrics.DAY_TEMP_KEY)).isEqualTo(11.0);
        Assertions.assertThat(map.get(WeatherMetrics.NIGHT_TEMP_KEY)).isEqualTo(9.0);
        Assertions.assertThat(map.get(WeatherMetrics.PRESSURE_KEY)).isEqualTo(1000.0);

        Map<String, Double> map1 = weatherMetrics.getPerDayMetrics().get("2019-02-28");
        Assertions.assertThat(map1.get(WeatherMetrics.DAY_TEMP_KEY)).isEqualTo(11.0);
        Assertions.assertThat(map1.get(WeatherMetrics.NIGHT_TEMP_KEY)).isEqualTo(9.0);
        Assertions.assertThat(map1.get(WeatherMetrics.PRESSURE_KEY)).isEqualTo(1000.0);

        Map<String, Double> map2 = weatherMetrics.getPerDayMetrics().get("2019-03-02");
        Assertions.assertThat(map2.get(WeatherMetrics.NIGHT_TEMP_KEY)).isEqualTo(9.0);
        Assertions.assertThat(map2.get(WeatherMetrics.PRESSURE_KEY)).isEqualTo(1000.0);
    }

    @Test(expected = IllegalStateException.class)
    public void testBadResponse() throws IOException {
        Mockito.when(weatherDataService.getWeatherData(Mockito.anyString())).thenReturn("bad response");
        weatherMetricsService.getWeatherMetrics("London,uk");
    }

    public static String readResourse(String resourceName) {
        try {
            return new String(Files.readAllBytes(Paths.get(WeatherMetricsApplicationTests.class.getResource(resourceName).toURI())));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

}
