package com.example.defecttrackerserver.core.defect.defectType;

import com.example.defecttrackerserver.core.defect.defectType.defectTypeException.DefectTypeExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefectTypeServiceImpl implements DefectTypeService {
    private final DefectTypeRepository defectTypeRepository;
    private final DefectTypeMapper defectTypeMapper;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public DefectTypeDto saveDefectType(@Valid DefectTypeDto defectTypeDto) {
        if(defectTypeRepository.findByName(defectTypeDto.getName()).isPresent())
            throw new DefectTypeExistsException("DefectType already exists with name: " + defectTypeDto.getName());

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
    public DefectTypeDto updateDefectType(@Valid DefectTypeDto defectTypeDto) {
        if(defectTypeDto.getId() == null)
            throw new IllegalArgumentException("DefectType id must not be null");

        DefectType defectType = defectTypeRepository.findById(defectTypeDto.getId())
                .orElseThrow(()-> new EntityNotFoundException("DefectType not found with id: "
                        + defectTypeDto.getId()));

        Optional<DefectType> defectTypeExists = defectTypeRepository.findByName(defectTypeDto.getName());
        if(defectTypeExists.isPresent() && !defectTypeExists.get().getId().equals(defectType.getId()))
            throw new DefectTypeExistsException("DefectType already exists with name: " + defectTypeDto.getName());

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
