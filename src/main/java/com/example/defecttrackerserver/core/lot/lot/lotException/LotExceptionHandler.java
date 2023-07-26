package com.example.defecttrackerserver.core.lot.lot.lotException;

import com.example.defecttrackerserver.exception.ExceptionDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
@Slf4j
public class LotExceptionHandler {

    @ExceptionHandler({LotExistsException.class})
    public ResponseEntity<Object> handleUserException(LotExistsException e) {
        ExceptionDetails exceptionDetails = new ExceptionDetails(
                Arrays.asList(e.getMessage()),
                e.getCause(),
                HttpStatus.NOT_FOUND
        );
        log.error("LotExistsException: ", e.getMessage());
        return new ResponseEntity<>(exceptionDetails, exceptionDetails.getHttpStatus());
    }
}
