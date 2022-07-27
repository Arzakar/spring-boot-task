package com.rntgroup.exception;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Data
@Accessors(chain = true)
public class CelestialBodyExceptionResponse {

    private HttpStatus status;
    private String message;
    private String stackTrace;

}
