package com.example.defecttrackerserver.core.lot.supplier.supplierException;

import com.example.defecttrackerserver.exception.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
public class SupplierExceptionHandler {

    @ExceptionHandler({SupplierExistsException.class})
    public ResponseEntity<Object> handleUserException(SupplierExistsException e) {
        ExceptionDetails exceptionDetails = new ExceptionDetails(
                Arrays.asList(e.getMessage()),
                e.getCause(),
                HttpStatus.NOT_FOUND
        );
        return new ResponseEntity<>(exceptionDetails, exceptionDetails.getHttpStatus());
    }
}
