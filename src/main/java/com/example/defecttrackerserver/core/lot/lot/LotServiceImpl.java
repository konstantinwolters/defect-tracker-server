package com.example.defecttrackerserver.core.lot.lot;

import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.lot.lot.dto.LotDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link LotService}.
 */
@Service
@RequiredArgsConstructor
class LotServiceImpl implements LotService{
    private final LotRepository lotRepository;
    private final LotMapper lotMapper;

    @Override
    @PreAuthorize("hasRole('ROLE_PURCHASER') or hasRole('ROLE_QA') or hasRole('ROLE_ADMIN') ")
    public LotDto saveLot(LotDto lotDto) {
        if(lotRepository.findByLotNumber(lotDto.getLotNumber()).isPresent())
            throw new DuplicateKeyException("Lot already exists with lot number: " + lotDto.getLotNumber());

        Lot newLot = lotMapper.map(lotDto, new Lot());

        return lotMapper.mapToDto(lotRepository.save(newLot));
    }

    @Override
    public LotDto getLotById(Integer id) {
        Lot lot = findLotById(id);
        return lotMapper.mapToDto(lot);
    }

    @Override
    public List<LotDto> getAllLots() {
        return lotRepository.findAll().stream().map(lotMapper::mapToDto).toList();
    }

    @Override
    @PreAuthorize("hasRole('ROLE_QA') or hasRole('ROLE_ADMIN')")
    public LotDto updateLot(Integer lotId, LotDto lotDto) {
        Lot lot = findLotById(lotId);

        Optional<Lot> lotExists = lotRepository.findByLotNumber(lotDto.getLotNumber());
        if(lotExists.isPresent() && !lotExists.get().getId().equals(lotDto.getId()))
            throw new DuplicateKeyException("Lot already exists with lot number: " + lotDto.getLotNumber());

        Lot mappedLot = lotMapper.map(lotDto, lot);

        return lotMapper.mapToDto(lotRepository.save(mappedLot));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteLot(Integer id) {
        Lot lot = findLotById(id);

        for (Defect defect : new ArrayList<>(lot.getDefects())) {
            lot.removeDefect(defect);
        }

        lotRepository.delete(lot);
    }

    private Lot findLotById(Integer id){
        return lotRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Lot not found with id: " + id));
    }
}
