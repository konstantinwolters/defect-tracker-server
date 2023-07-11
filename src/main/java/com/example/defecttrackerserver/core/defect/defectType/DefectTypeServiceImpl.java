package com.example.defecttrackerserver.core.defect.defectType;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefectTypeServiceImpl implements DefectTypeService {
    private final DefectTypeRepository defectTypeRepository;
    private final DefectTypeMapper defectTypeMapper;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public DefectTypeDto saveDefectType(DefectTypeDto defectTypeDto) {
        if(defectTypeDto.getName() == null)
            throw new IllegalArgumentException("DefectType name must not be null");

        DefectType defectType = new DefectType();
        defectType.setName(defectTypeDto.getName());

        DefectType savedDefectType = defectTypeRepository.save(defectType);

        return defectTypeMapper.mapToDto(savedDefectType);
    }

    @Override
    public DefectTypeDto getDefectTypeById(Integer id) {
        return defectTypeRepository.findById(id)
                .map(defectTypeMapper::mapToDto)
                .orElseThrow(() -> new IllegalArgumentException("DefectType not found with id: " + id));
    }

    @Override
    public List<DefectTypeDto> getAllDefectTypes() {
        return defectTypeRepository.findAll()
                .stream()
                .map(defectTypeMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

        return defectTypeMapper.mapToDto(saveDefectType);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteDefectType(Integer id) {
        DefectType defectType = defectTypeRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("DefectType not found with id: " + id));

        defectTypeRepository.delete(defectType);
    }
}
