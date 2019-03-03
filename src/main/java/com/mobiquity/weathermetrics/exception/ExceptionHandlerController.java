package com.mobiquity.weathermetrics.exception;

import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Optional;

/**
 * Global Exception handling for all requests.
 * Further, WeatherMetricsNotFoundException is created for 404.
 */
@ControllerAdvice("com.mobiquity.weathermetrics")
public class ExceptionHandlerController {

    //404 - Not found
    @ExceptionHandler(WeatherMetricsNotFoundException.class)
    public ResponseEntity<VndErrors> handleNotFound(final WeatherMetricsNotFoundException e) {
        return error(e, HttpStatus.NOT_FOUND, e.getId());
    }

    //400 - Bad request
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<VndErrors> handleBadRequest(final IllegalArgumentException e) {
        return error(e, HttpStatus.NOT_FOUND, e.getLocalizedMessage());
    }

    // 500 - All other error as Internal error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<VndErrors> handleAllOtherError(final Exception e) {
        return error(e, HttpStatus.NOT_FOUND, e.getLocalizedMessage());
    }


    private ResponseEntity<VndErrors> error(final Exception exception, final HttpStatus httpStatus, final String logRef) {
        final String message = Optional.of(exception.getMessage()).orElse(exception.getClass().getSimpleName());
        return new ResponseEntity<>(new VndErrors(logRef, message), httpStatus);
    }
}
