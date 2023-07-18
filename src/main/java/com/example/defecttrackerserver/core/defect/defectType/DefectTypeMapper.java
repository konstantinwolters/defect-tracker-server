package com.example.defecttrackerserver.core.defect.defectType;

import org.springframework.stereotype.Component;

@Component
public class DefectTypeMapper {
    public DefectTypeDto mapToDto(DefectType defectType) {
        DefectTypeDto defectTypeDto = new DefectTypeDto();
        defectTypeDto.setId(defectType.getId());
        defectTypeDto.setName(defectType.getName());
        return defectTypeDto;
    }
}
