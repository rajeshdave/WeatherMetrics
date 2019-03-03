# Weather Metrics

## Overview
This is API that will retrieve weather metrics of a specific city.
We are using Open Weather Map API for collecting Weather forecast data.
[Refer here for more details.](https://openweathermap.org/)

## Steps to Setup and Run

**1. Clone the repository** 

```bash
git clone https://github.com/rajeshdave/WeatherMetrics.git
```

**2. How to Run**

You may package the application in the form of a jar and then run the jar file like so -

```bash
mvn clean package
java -jar target/WeatherMetrics-0.0.1-SNAPSHOT.jar
```

That's it! The application can be accessed at `http://localhost:4000/weather?city=London,uk`.

## Weather Metrics Details
We are using [Open Weather Forecast API](https://openweathermap.org/forecast5) to retrieve forecast data and generate below metrics:
* Average of daily (06:00 – 18:00) and nightly (18:00 – 06:00) temperatures in
Celsius for the next 3 days from today’s date.
* Average of pressure for the next 3 days from today’s date.

The endpoint must include a CITY parameter containing the city’s name as the input for
the correct response.
* For example, `http://localhost:4000/weather?city=London,uk` will return below output:

```json
{
  "perDayMetrics": {
    "2019-03-03": {
      "NightTemperature": 10.03,
      "DayTemperature": 11.935,
      "Pressure": 994.8199999999999
    },
    "2019-03-04": {
      "NightTemperature": 6.835,
      "DayTemperature": 6.5,
      "Pressure": 992.3575
    },
    "2019-03-05": {
      "NightTemperature": 5.109999999999999,
      "DayTemperature": 6.215,
      "Pressure": 999.41
    }
  }
}
```
We will be hitting below URL, which is returning data for every 3 hours.
 URL: `http://api.openweathermap.org/data/2.5/forecast?q=London,uk&units=metric&cnt=24&APPID=4d8d9da673e70c41ef740081db5e4a10`
We are passing 'cnt=24' to collect next 3 days records. In case, we are getting data of fourth day then we are just ignoring same.


## Code and Testcase Details
This is Spring boot application using Spring Boot v2.1.3.RELEASE and Spring v5.1.5.RELEASE.
* `WeatherMetricsApplication.java` is Spring boot application class.
* `OpenWeatherController` is REST controller to handle all requests.
* `WeatherMetricsService` is responsible for building weather metrics by using data from `WeatherDataService`.
* `WeatherDataService` is to make REST call to collect forecast data.
* `ExceptionHandlerController` is for handling all exceptions. `WeatherMetricsNotFoundException` is custom exception.

Below are test cases added to test the code:
* `WeatherMetricsApplicationTests` for doing end to end integration tests. This cover testcases for both successful and exception scenarios.
* `OpenWeatherMetricsServiceTest` is added to do unit testing of Metrics logic by mocking forecast data response.

## Motivations
Below are motivations behind given design of application:
* Clean code
* Single Responsibility by layered design. Like  `OpenWeatherController`, `WeatherMetricsService`, `WeatherDataService` etc.
* Isolation by use of Interfaces.
* Global Exception handling with REST standard error response. 
* Unit and integration Test cases with nearly 100% Code coverage.
* No issue based on Sonar analysis.
* For more details please refer javadoc of each class.


> “It is not enough for code to work.” 
> ― Robert C. Martin, Clean Code: A Handbook of Agile Software Craftsmanship