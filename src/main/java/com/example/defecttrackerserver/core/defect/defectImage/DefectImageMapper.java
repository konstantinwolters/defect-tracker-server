package com.example.defecttrackerserver.core.defect.defectImage;

import org.springframework.stereotype.Component;

/**
 * Provides methods to map entity objects to DTOs and vice versa.
 */
@Component
public class DefectImageMapper {

    public DefectImageDto mapToDto(DefectImage defectImage) {
        DefectImageDto defectImageDto = new DefectImageDto();
        defectImageDto.setId(defectImage.getId());
        defectImageDto.setUuid(defectImage.getUuid());
        return defectImageDto;
    }
}
