package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusRepository;
import com.example.defecttrackerserver.core.lot.lot.Lot;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefectServiceImpl implements DefectService{
    private final DefectRepository defectRepository;
    private final DefectStatusRepository defectStatusRepository;
    private final ModelMapper modelMapper;
    private final DefectMapper defectMapper;

    @Override
    @Transactional
    public DefectDto saveDefect(DefectDto defectDto) {
        Defect defect = new Defect();
        defectDto.setId(null);
        defectDto.setCreatedOn(LocalDateTime.now());

        Defect newDefect = defectMapper.map(defectDto, defect);
        //TODO: Set Status?! Must first be fetched from DB

        return modelMapper.map(defectRepository.save(newDefect), DefectDto.class);
    }

    @Override
    public DefectDto getDefectById(Integer id) {
        Defect defect = defectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Defect not found with id: " + id));
        return modelMapper.map(defect, DefectDto.class);
    }

    @Override
    public List<DefectDto> getAllDefects() {
        return defectRepository.findAll().stream().map(defect -> modelMapper.map(defect, DefectDto.class)).toList();
    }

    @Override
    @Transactional
    public DefectDto updateDefect(DefectDto defectDto) {
        Defect defectToUpdate = defectRepository.findById(defectDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Defect not found with id: " + defectDto.getId()));

        defectToUpdate.setDefectStatus(defectStatusRepository.findByName(defectDto.getDefectStatus())
                .orElseThrow(() -> new EntityNotFoundException("Defect Status not found with name: "
                        + defectDto.getDefectStatus())));
        Defect mappedDefect = defectMapper.map(defectDto, defectToUpdate);

        Defect updatedDefect = defectRepository.save(mappedDefect);
        return modelMapper.map(updatedDefect, DefectDto.class);
    }

    @Override
    @Transactional
    public void deleteDefect(Integer id) {
        Defect defectToDelete = defectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Defect not found with id: " + id));

        Lot lot = defectToDelete.getLot();
        lot.removeDefect(defectToDelete);

        for (Action action : new ArrayList<>(defectToDelete.getActions())) {
            defectToDelete.deleteAction(action);
        }

        defectRepository.delete(defectToDelete);
    }
}
