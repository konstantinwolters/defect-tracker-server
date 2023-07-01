package com.example.defecttrackerserver.core.lot.lot;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.lot.lot.lotException.LotExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LotServiceImpl implements LotService{
    private final LotRepository lotRepository;
    private final LotMapper lotMapper;
    private final ModelMapper modelMapper;

    @Override
    public LotDto saveLot(LotDto lotDto) {
        if(lotRepository.findByLotNumber(lotDto.getLotNumber()).isPresent())
            throw new LotExistsException("Lot already exists with lot number: " + lotDto.getLotNumber());

        @SuppressWarnings("ConstantConditions")
        Lot newLot = lotMapper.map(lotDto, new Lot());

        return modelMapper.map(lotRepository.save(newLot), LotDto.class);
    }

    @Override
    public LotDto getLotById(Integer id) {
        Lot lot = lotRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Lot not found with id: " + id));

        return modelMapper.map(lot, LotDto.class);
    }

    @Override
    public List<LotDto> getAllLots() {
        return lotRepository.findAll().stream().map(lot -> modelMapper.map(lot, LotDto.class)).toList();
    }

    @Override
    public LotDto updateLot(LotDto lotDto) {
        Lot lot = lotRepository.findById(lotDto.getId())
                .orElseThrow(()-> new EntityNotFoundException("Lot not found with id: " + lotDto.getId()));

        Optional<Lot> lotExists = lotRepository.findByLotNumber(lotDto.getLotNumber());
        if(lotExists.isPresent() && !lotExists.get().getId().equals(lotDto.getId()))
            throw new LotExistsException("Lot already exists with lot number: " + lotDto.getLotNumber());

        Lot mappedLot = lotMapper.map(lotDto, lot);

        return modelMapper.map(lotRepository.save(mappedLot), LotDto.class);
    }

    @Override
    @Transactional
    public void deleteLot(Integer id) {
        Lot lot = lotRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Lot not found with id: " + id));

        for (Defect defect : new ArrayList<>(lot.getDefects())) {
            lot.removeDefect(defect);
        }

        lotRepository.delete(lot);
    }
}
