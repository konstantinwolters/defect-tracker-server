package com.example.defecttrackerserver.core.lot.lot;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.defect.defect.Defect;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LotServiceImpl implements LotService{
    private final LotRepository lotRepository;
    private final LotMapper lotMapper;
    private final ModelMapper modelMapper;
    @Override
    public LotDto saveLot(LotDto lotDto) {
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

        Lot mappedLot = lotMapper.map(lotDto, lot);

        return modelMapper.map(lotRepository.save(mappedLot), LotDto.class);
    }

    @Override
    public void deleteLot(Integer id) {
        Lot lot = lotRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Lot not found with id: " + id));

        for (Defect defect : new ArrayList<>(lot.getDefects())) {
            lot.removeDefect(defect);
        }

        lotRepository.delete(lot);
    }
}