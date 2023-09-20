package com.example.defecttrackerserver.core.defect.defectStatus;

import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link DefectStatusService}.
 */
@Service
@RequiredArgsConstructor
public class DefectStatusServiceImpl implements DefectStatusService{
    private final DefectStatusRepository defectStatusRepository;
    private final DefectRepository defectRepository;
    private final DefectStatusMapper defectStatusMapper;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public DefectStatusDto saveDefectStatus(DefectStatusDto defectStatusDto) {
        if(defectStatusRepository.findByName(defectStatusDto.getName()).isPresent())
            throw new DuplicateKeyException("DefectStatus already exists with name: " + defectStatusDto.getName());

        DefectStatus defectStatus = new DefectStatus();
        defectStatus.setName(defectStatusDto.getName());

        DefectStatus savedDefectStatus = defectStatusRepository.save(defectStatus);

        return defectStatusMapper.mapToDto(savedDefectStatus);
    }

    @Override
    public DefectStatusDto getDefectStatusById(Integer id) {
        DefectStatus defectStatus = findDefectStatusById(id);
        return defectStatusMapper.mapToDto(defectStatus);
    }

    @Override
    public List<DefectStatusDto> getAllDefectStatus() {
        return defectStatusRepository.findAll()
                .stream()
                .map(defectStatusMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public DefectStatusDto updateDefectStatus(Integer defectStatusId, DefectStatusDto defectStatusDto) {
        DefectStatus defectStatus = findDefectStatusById(defectStatusId);

        if(defectStatusRepository.findByName(defectStatusDto.getName()).isPresent()
                && !defectStatusRepository.findByName(defectStatusDto.getName()).get().getId().equals(defectStatus.getId()))
            throw new DuplicateKeyException("DefectStatus already exists with name: " + defectStatusDto.getName());

        defectStatus.setName(defectStatusDto.getName());
        DefectStatus savedDefectStatus = defectStatusRepository.save(defectStatus);

        return defectStatusMapper.mapToDto(savedDefectStatus);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteDefectStatus(Integer id) {
        DefectStatus defectStatus = findDefectStatusById(id);

        if(!defectRepository.findByDefectStatusId(id).isEmpty())
            throw new UnsupportedOperationException("DefectStatus cannot be deleted because it is used in Defects");

        //'New' must not be deleted because it is a default value
        if(defectStatus.getName().equals("New"))
            throw new UnsupportedOperationException("DefectStatus 'New' cannot be deleted because it " +
                    "is a default value.");

        defectStatusRepository.delete(defectStatus);
    }

    private DefectStatus findDefectStatusById(Integer id) {
        return defectStatusRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("DefectStatus not found with id: " + id));
    }
}
