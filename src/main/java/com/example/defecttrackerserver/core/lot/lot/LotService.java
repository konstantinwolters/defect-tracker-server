package com.example.defecttrackerserver.core.lot.lot;

import com.example.defecttrackerserver.core.location.LocationDto;

import java.util.List;

public interface LotService {
    LotDto saveLot(LotDto lotDto);
    LotDto getLotById(Integer id);
    List<LotDto> getAllLots();
    LotDto updateLot(LotDto lotDto);
    void deleteLot(Integer id);
}
