package com.example.defecttrackerserver.core.lot.supplier;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierDto {
    private Integer id;
    private String customId;

    @NotNull(message = "Supplier name must not be null.")
    @NotEmpty(message = "Supplier name must not be empty.")
    private String name;
}
