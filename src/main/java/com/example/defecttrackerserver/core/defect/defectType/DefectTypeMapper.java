package com.example.defecttrackerserver.core.defect.defectType;

import org.springframework.stereotype.Component;

/**
 * Provides methods to map entity objects to DTOs and vice versa.
 */
@Component
public class DefectTypeMapper {
    public DefectTypeDto mapToDto(DefectType defectType) {
        DefectTypeDto defectTypeDto = new DefectTypeDto();
        defectTypeDto.setId(defectType.getId());
        defectTypeDto.setName(defectType.getName());
        return defectTypeDto;
    }
}
