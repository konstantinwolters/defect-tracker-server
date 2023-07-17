package com.example.defecttrackerserver.core.defect.defectType.defectTypeException;

import com.example.defecttrackerserver.exception.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefectTypeExceptionHandler {

    @ExceptionHandler({DefectTypeExistsException.class})
    public ResponseEntity<Object> handleUserException(DefectTypeExistsException e) {
        ExceptionDetails exceptionDetails = new ExceptionDetails(
                e.getMessage(),
                e.getCause(),
                HttpStatus.NOT_FOUND
        );
        return new ResponseEntity<>(exceptionDetails, exceptionDetails.getHttpStatus());
    }
}
