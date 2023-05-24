package com.example.defecttrackerserver.core.material;

import com.example.defecttrackerserver.core.material.lot.Lot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @OneToMany(
            mappedBy = "material",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Lot> lots = new ArrayList<>();

    public void addLot(Lot lot){
        lots.add(lot);
    }

    public void removeLot(Lot lot){
        lots.remove(lot);
        lot.setMaterial(null);
    }

}
