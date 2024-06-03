package com.example.defecttrackerserver.core.defect.defectType;

import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of {@link DefectTypeService}.
 */
@Service
@RequiredArgsConstructor
class DefectTypeServiceImpl implements DefectTypeService {
    private final DefectTypeRepository defectTypeRepository;
    private final DefectRepository defectRepository;
    private final DefectTypeMapper defectTypeMapper;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public DefectTypeDto saveDefectType(@Valid DefectTypeDto defectTypeDto) {
        if(defectTypeRepository.findByName(defectTypeDto.getName()).isPresent())
            throw new DuplicateKeyException("DefectType already exists with name: " + defectTypeDto.getName());

        DefectType defectType = new DefectType();
        defectType.setName(defectTypeDto.getName());

        DefectType savedDefectType = defectTypeRepository.save(defectType);

        return defectTypeMapper.mapToDto(savedDefectType);
    }

    @Override
    public DefectTypeDto getDefectTypeById(Integer id) {
        DefectType defectType = findDefectTypeById(id);
        return defectTypeMapper.mapToDto(defectType);
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
    public DefectTypeDto updateDefectType(Integer defectTypeId, @Valid DefectTypeDto defectTypeDto) {

        DefectType defectType = findDefectTypeById(defectTypeId);

        Optional<DefectType> defectTypeExists = defectTypeRepository.findByName(defectTypeDto.getName());
        if(defectTypeExists.isPresent() && !defectTypeExists.get().getId().equals(defectType.getId()))
            throw new DuplicateKeyException("DefectType already exists with name: " + defectTypeDto.getName());

        defectType.setName(defectTypeDto.getName());

        DefectType saveDefectType = defectTypeRepository.save(defectType);

        return defectTypeMapper.mapToDto(saveDefectType);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteDefectType(Integer id) {
        DefectType defectType = findDefectTypeById(id);

        if(!defectRepository.findByDefectTypeId(id).isEmpty())
            throw new UnsupportedOperationException("DefectType cannot be deleted because it is used in Defects");

        if(defectType.getName().equals("Undefined"))
            throw new UnsupportedOperationException("DefectType 'Undefined' cannot be deleted " +
                    "because it is a default value.");

        defectTypeRepository.delete(defectType);
    }

    private DefectType findDefectTypeById(Integer id) {
        return defectTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DefectType not found with id: " + id));
    }
}
