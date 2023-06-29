package com.example.defecttrackerserver.core.lot.supplier;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierDto {
    private Integer id;
    private String name;
}
