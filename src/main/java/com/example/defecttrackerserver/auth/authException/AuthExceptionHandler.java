package com.example.defecttrackerserver.auth.authException;

import com.example.defecttrackerserver.exception.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler({UnauthorizedAccessException.class})
    public ResponseEntity<Object> handleUserException(UnauthorizedAccessException e) {
        ExceptionDetails exceptionDetails = new ExceptionDetails(
                e.getMessage(),
                e.getCause(),
                HttpStatus.FORBIDDEN
        );
        return new ResponseEntity<>(exceptionDetails, exceptionDetails.getHttpStatus());
    }
}
