package com.example.defecttrackerserver.core.material.lot;

import com.example.defecttrackerserver.core.defect.Defect;
import com.example.defecttrackerserver.core.material.Material;
import com.example.defecttrackerserver.core.material.supplier.Supplier;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private Material material;

    @ManyToOne(fetch = FetchType.LAZY)
    private Supplier supplier;

    @OneToMany(mappedBy = "lot",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Defect> defects = new ArrayList<>();
}
