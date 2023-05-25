package com.example.defecttrackerserver.core.material.lot;

import com.example.defecttrackerserver.core.defect.Defect;
import com.example.defecttrackerserver.core.material.Material;
import com.example.defecttrackerserver.core.material.supplier.Supplier;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "defect_id")
    private Defect defect;
}
