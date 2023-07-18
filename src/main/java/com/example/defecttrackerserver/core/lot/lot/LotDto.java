package com.example.defecttrackerserver.core.lot.lot;

import com.example.defecttrackerserver.core.lot.material.MaterialDto;
import com.example.defecttrackerserver.core.lot.supplier.SupplierDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class LotDto {
    private Integer id;
    private Set<Integer> defects;

    @NotNull(message = "Lot number must not be null")
    @NotEmpty(message = "Lot number must not be empty")
    private String lotNumber;

    @NotNull(message = "Lot material must not be null.")
    private MaterialDto material;

    @NotNull(message = "Lot supplier must not be null.")
    private SupplierDto supplier;
}
