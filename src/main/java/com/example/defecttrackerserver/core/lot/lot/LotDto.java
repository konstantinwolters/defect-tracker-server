package com.example.defecttrackerserver.core.lot.lot;

import com.example.defecttrackerserver.core.lot.material.MaterialDto;
import com.example.defecttrackerserver.core.lot.supplier.SupplierDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class LotDto {
    private Integer id;
    private String lotNumber;
    private MaterialDto material;
    private SupplierDto supplier;
    private Set<Integer> defects;
}
