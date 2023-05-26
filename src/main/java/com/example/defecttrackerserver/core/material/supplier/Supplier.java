package com.example.defecttrackerserver.core.material.supplier;

import com.example.defecttrackerserver.core.material.lot.Lot;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Supplier {

    @Id
    private Integer id;
    private String name;

    @OneToMany(
            mappedBy = "supplier",
            cascade = CascadeType.PERSIST)
    private List<Lot> lots = new ArrayList<>();

    public void addLot(Lot lot){
        lots.add(lot);
        lot.setSupplier(this);
    }

    public void removeLot(Lot lot){
        lots.remove(lot);
        lot.setSupplier(null);
    }
}
