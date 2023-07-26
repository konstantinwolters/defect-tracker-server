package com.example.defecttrackerserver.core.location.locationException;

import com.example.defecttrackerserver.exception.ExceptionDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
@Slf4j
public class LocationExceptionHandler {

    @ExceptionHandler({LocationExistsException.class})
    public ResponseEntity<Object> handleUserException(LocationExistsException e) {
        ExceptionDetails exceptionDetails = new ExceptionDetails(
                Arrays.asList(e.getMessage()),
                e.getCause(),
                HttpStatus.NOT_FOUND
        );
        log.error("LocationExistsException: ", e.getMessage());
        return new ResponseEntity<>(exceptionDetails, exceptionDetails.getHttpStatus());
    }
}
