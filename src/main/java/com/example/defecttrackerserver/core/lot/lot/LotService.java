package com.example.defecttrackerserver.core.lot.lot;

import com.example.defecttrackerserver.core.lot.lot.dto.LotDto;

import java.util.List;

/**
 * Service interface for managingt {@link Lot}.
 */
public interface LotService {
    LotDto saveLot(LotDto lotDto);
    LotDto getLotById(Integer id);
    List<LotDto> getAllLots();
    LotDto updateLot(Integer lodId, LotDto lotDto);
    void deleteLot(Integer id);
}
