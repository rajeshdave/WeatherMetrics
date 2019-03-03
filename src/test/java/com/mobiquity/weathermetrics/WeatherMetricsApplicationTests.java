package com.mobiquity.weathermetrics;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * End to end Integration test case for both successful and exception scenarios.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Slf4j
public class WeatherMetricsApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testSuccessfulResponse() {
        String city = "London,uk";
        String metrics = restTemplate.getForObject("/weather?city=" + city, String.class);
        log.info(">>> Metrics for London: " + metrics);
    }

    @Test
    public void testWithEmptyCityName() {
        String city = "";
        String metrics = restTemplate.getForObject("/weather?city=" + city, String.class);
        log.info(">>> Metrics for London: " + metrics);
        String expectedError = "[{\"logref\":\"400 - Bad request. Request param city is mandatory.\"," +
                "\"message\":\"400 - Bad request. Request param city is mandatory.\",\"links\":[]}]";
        Assert.assertEquals(expectedError, metrics);
    }

    @Test
    public void testNotFound() {
        String city = "xyz";
        String metrics = restTemplate.getForObject("/weather?city=" + city, String.class);
        log.info(">>> Metrics for London: " + metrics);
        String expectedError = "[{\"logref\":\"WEATHER_METRICS_E001\",\"message\":\"404 Not found. Weather data not found for xyz\",\"links\":[]}]";
        Assert.assertEquals(expectedError, metrics);
    }

}
