package com.example.defecttrackerserver.core.defect.defectStatus;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefectStatusServiceImpl implements DefectStatusService{
    private final DefectStatusRepository defectStatusRepository;
    private final DefectStatusMapper defectStatusMapper;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public DefectStatusDto saveDefectStatus(DefectStatusDto defectStatusDto) {
        if(defectStatusDto.getName() == null)
            throw new IllegalArgumentException("DefectStatus name must not be null");

        DefectStatus defectStatus = new DefectStatus();
        defectStatus.setName(defectStatusDto.getName());

        DefectStatus savedDefectStatus = defectStatusRepository.save(defectStatus);

        return defectStatusMapper.mapToDto(savedDefectStatus);
    }

    @Override
    public DefectStatusDto getDefectStatusById(Integer id) {
        return defectStatusRepository.findById(id)
                .map(defectStatusMapper::mapToDto)
                .orElseThrow(() -> new IllegalArgumentException("DefectStatus not found with id: " + id));
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
    public DefectStatusDto updateDefectStatus(DefectStatusDto defectStatusDto) {
        if(defectStatusDto.getId() == null)
            throw new IllegalArgumentException("DefectStatus id must not be null");
        if(defectStatusDto.getName() == null)
            throw new IllegalArgumentException("DefectStatus name must not be null");

        DefectStatus defectStatus = defectStatusRepository.findById(defectStatusDto.getId())
                .orElseThrow(()-> new EntityNotFoundException("DefectStatus not found with id: "
                        + defectStatusDto.getId()));

        defectStatus.setName(defectStatusDto.getName());
        DefectStatus savedDefectStatus = defectStatusRepository.save(defectStatus);

        return defectStatusMapper.mapToDto(savedDefectStatus);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteDefectStatus(Integer id) {
        DefectStatus defectStatus = defectStatusRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("DefectStatus not found with id: " + id));

        defectStatusRepository.delete(defectStatus);
    }
}
