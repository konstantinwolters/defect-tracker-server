package com.example.defecttrackerserver.exception;

import com.example.defecttrackerserver.auth.authException.UnauthorizedAccessException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        return createResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException e) {
        return createResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException e) {
        return createResponse(e, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<Object> handleUnauthorizedAccessException(UnauthorizedAccessException e) {
        return createResponse(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        return new ResponseEntity<>(new ExceptionDetails(errors, null, HttpStatus.BAD_REQUEST)
                , HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> createResponse(Exception e, HttpStatus status) {
        ExceptionDetails exceptionDetails = new ExceptionDetails(
                Arrays.asList(e.getMessage()),
                e.getCause(),
                status
        );
        return new ResponseEntity<>(exceptionDetails, exceptionDetails.getHttpStatus());
    }
}
