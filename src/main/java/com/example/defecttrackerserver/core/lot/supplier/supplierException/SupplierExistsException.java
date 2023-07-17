package com.example.defecttrackerserver.core.lot.supplier.supplierException;

import lombok.Getter;

@Getter
public class SupplierExistsException extends RuntimeException{
    public SupplierExistsException(String message) {
        super(message);
    }

    public SupplierExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
