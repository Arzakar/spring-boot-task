package com.rntgroup.exception;

import com.rntgroup.exception.CelestialBodyException.CelestialBodyNotFoundByNameException;
import com.rntgroup.exception.CelestialBodyException.CelestialBodyBadRequestException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CelestialBodyControllerAdvice {

    @ExceptionHandler(CelestialBodyNotFoundByNameException.class)
    public ResponseEntity<CelestialBodyExceptionResponse> handleException(CelestialBodyNotFoundByNameException exception) {
        var response = new CelestialBodyExceptionResponse()
                .setStatus(HttpStatus.NOT_FOUND)
                .setMessage(exception.getMessage());
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(CelestialBodyBadRequestException.class)
    public ResponseEntity<CelestialBodyExceptionResponse> handleException(CelestialBodyBadRequestException exception) {
        var response = new CelestialBodyExceptionResponse()
                .setStatus(HttpStatus.BAD_REQUEST)
                .setMessage(exception.getMessage());
        return new ResponseEntity<>(response, response.getStatus());
    }
}
