package com.example.defecttrackerserver.core.lot.lot.dto;

import com.example.defecttrackerserver.core.lot.lot.Lot;
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
