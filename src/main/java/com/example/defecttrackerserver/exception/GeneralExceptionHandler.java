package com.example.defecttrackerserver.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GeneralExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception e) {
        log.error("Exception occurred: ", e);
        return createResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Invalid argument provided: ", e);
        return createResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("Entity not found: ", e);
        return createResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException e) {
        log.error("Access denied: ", e);
        return createResponse(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("Invalid arguments: ", e);
        return createResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("Method not supported: ", e);
        return createResponse(e, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Invalid arguments: ", ex);
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
