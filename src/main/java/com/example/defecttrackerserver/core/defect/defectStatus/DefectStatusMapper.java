package com.example.defecttrackerserver.core.defect.defectStatus;

import org.springframework.stereotype.Component;

@Component
public class DefectStatusMapper {
    public DefectStatusDto mapToDto(DefectStatus defectStatus) {
        DefectStatusDto defectStatusDto = new DefectStatusDto();
        defectStatusDto.setId(defectStatus.getId());
        defectStatusDto.setName(defectStatus.getName());
        return defectStatusDto;
    }
}
