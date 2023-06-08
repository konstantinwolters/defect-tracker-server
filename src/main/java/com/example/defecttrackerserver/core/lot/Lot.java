package com.example.defecttrackerserver.core.lot;

import com.example.defecttrackerserver.core.defect.Defect;
import com.example.defecttrackerserver.core.lot.material.Material;
import com.example.defecttrackerserver.core.lot.supplier.Supplier;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Lot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Material material;

    @ManyToOne
    private Supplier supplier;

    @OneToMany(mappedBy = "lot",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Defect> defects = new ArrayList<>();
}
