package com.example.defecttrackerserver.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        ExceptionDetails exceptionDetails = new ExceptionDetails(
                e.getMessage(),
                e.getCause(),
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(exceptionDetails, exceptionDetails.getHttpStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException e) {
        ExceptionDetails exceptionDetails = new ExceptionDetails(
                e.getMessage(),
                e.getCause(),
                HttpStatus.NOT_FOUND
        );
        return new ResponseEntity<>(exceptionDetails, exceptionDetails.getHttpStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException e) {
        ExceptionDetails exceptionDetails = new ExceptionDetails(
                e.getMessage(),
                e.getCause(),
                HttpStatus.FORBIDDEN
        );
        return new ResponseEntity<>(exceptionDetails, exceptionDetails.getHttpStatus());
    }
}
