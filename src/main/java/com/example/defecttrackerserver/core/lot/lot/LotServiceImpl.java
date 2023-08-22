package com.example.defecttrackerserver.core.lot.lot;

import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.lot.lot.lotException.LotExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LotServiceImpl implements LotService{
    private final LotRepository lotRepository;
    private final LotMapper lotMapper;

    @Override
    @PreAuthorize("hasRole('ROLE_PURCHASER') or hasRole('ROLE_QA') or hasRole('ROLE_ADMIN') ")
    public LotDto saveLot(LotDto lotDto) {
        if(lotRepository.findByLotNumber(lotDto.getLotNumber()).isPresent())
            throw new LotExistsException("Lot already exists with lot number: " + lotDto.getLotNumber());

        Lot newLot = lotMapper.map(lotDto, new Lot());

        return lotMapper.mapToDto(lotRepository.save(newLot));
    }

    @Override
    public LotDto getLotById(Integer id) {
        Lot lot = lotRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Lot not found with id: " + id));

        return lotMapper.mapToDto(lot);
    }

    @Override
    public List<LotDto> getAllLots() {
        return lotRepository.findAll().stream().map(lotMapper::mapToDto).toList();
    }

    @Override
    @PreAuthorize("hasRole('ROLE_QA') or hasRole('ROLE_ADMIN')")
    public LotDto updateLot(Integer lotId, LotDto lotDto) {
        Lot lot = lotRepository.findById(lotId)
                .orElseThrow(()-> new EntityNotFoundException("Lot not found with id: " + lotId));

        Optional<Lot> lotExists = lotRepository.findByLotNumber(lotDto.getLotNumber());
        if(lotExists.isPresent() && !lotExists.get().getId().equals(lotDto.getId()))
            throw new LotExistsException("Lot already exists with lot number: " + lotDto.getLotNumber());

        Lot mappedLot = lotMapper.map(lotDto, lot);

        return lotMapper.mapToDto(lotRepository.save(mappedLot));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteLot(Integer id) {
        Lot lot = lotRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Lot not found with id: " + id));

        for (Defect defect : new ArrayList<>(lot.getDefects())) {
            lot.removeDefect(defect);
        }

        lotRepository.delete(lot);
    }
}
