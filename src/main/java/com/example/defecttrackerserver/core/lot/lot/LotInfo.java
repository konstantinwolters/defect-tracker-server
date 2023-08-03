package com.example.defecttrackerserver.core.lot.lot;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LotInfo {
    private Integer id;
    private String lotNumber;

    public LotInfo(Lot lot) {
        this.id = lot.getId();
        this.lotNumber = lot.getLotNumber();
    }
}
