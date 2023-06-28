package com.example.defecttrackerserver.core.defect.defectType;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefectTypeServiceImpl implements DefectTypeService {
    private final DefectTypeRepository defectTypeRepository;
    private final ModelMapper modelMapper;

    @Override
    public DefectTypeDto saveDefectType(DefectTypeDto defectTypeDto) {
        if(defectTypeDto.getName() == null)
            throw new IllegalArgumentException("DefectType name must not be null");

        DefectType defectType = new DefectType();
        defectType.setName(defectTypeDto.getName());

        DefectType savedDefectType = defectTypeRepository.save(defectType);

        return modelMapper.map(savedDefectType, DefectTypeDto.class);
    }

    @Override
    public DefectTypeDto getDefectTypeById(Integer id) {
        return defectTypeRepository.findById(id)
                .map(defectType -> modelMapper.map(defectType, DefectTypeDto.class))
                .orElseThrow(() -> new IllegalArgumentException("DefectType not found with id: " + id));
    }

    @Override
    public List<DefectTypeDto> getAllDefectTypes() {
        return defectTypeRepository.findAll()
                .stream()
                .map(defectType -> modelMapper.map(defectType, DefectTypeDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public DefectTypeDto updateDefectType(DefectTypeDto defectTypeDto) {
        if(defectTypeDto.getId() == null)
            throw new IllegalArgumentException("DefectType id must not be null");
        if(defectTypeDto.getName() == null)
            throw new IllegalArgumentException("DefectType name must not be null");

        DefectType defectType = defectTypeRepository.findById(defectTypeDto.getId())
                .orElseThrow(()-> new EntityNotFoundException("DefectType not found with id: "
                        + defectTypeDto.getId()));

        defectType.setName(defectTypeDto.getName());
        DefectType saveDefectType = defectTypeRepository.save(defectType);

        return modelMapper.map(saveDefectType, DefectTypeDto.class);
    }

    @Override
    public void deleteDefectType(Integer id) {
        defectTypeRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("DefectType not found with id: " + id));

        defectTypeRepository.deleteById(id);
    }
}
