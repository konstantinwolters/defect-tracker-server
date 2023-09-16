package com.example.defecttrackerserver.core.defect.defectStatus;

import org.springframework.stereotype.Component;

/**
 * Provides methods to map entity objects to DTOs and vice versa.
 */
@Component
public class DefectStatusMapper {
    public DefectStatusDto mapToDto(DefectStatus defectStatus) {
        DefectStatusDto defectStatusDto = new DefectStatusDto();
        defectStatusDto.setId(defectStatus.getId());
        defectStatusDto.setName(defectStatus.getName());
        return defectStatusDto;
    }
}
