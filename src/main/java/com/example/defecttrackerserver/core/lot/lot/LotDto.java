package com.example.defecttrackerserver.core.lot.lot;

import com.example.defecttrackerserver.core.lot.material.Material;
import com.example.defecttrackerserver.core.lot.supplier.Supplier;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class LotDto {

    private Integer id;
    private Material material;
    private Supplier supplier;
    private Set<Integer> defects = new HashSet<>();
}
