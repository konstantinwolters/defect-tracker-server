package com.example.defecttrackerserver.core.lot.lot;

import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.lot.material.Material;
import com.example.defecttrackerserver.core.lot.supplier.Supplier;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

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
            cascade = CascadeType.REMOVE,
            orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Defect> defects = new HashSet<>();

    public void addDefect(Defect defect) {
        defects.add(defect);
        defect.setLot(this);
    }

    public void removeDefect(Defect defect) {
        defects.remove(defect);
        defect.setLot(null);
    }
}
