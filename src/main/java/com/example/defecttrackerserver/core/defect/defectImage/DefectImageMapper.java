package com.example.defecttrackerserver.core.defect.defectImage;

import org.springframework.stereotype.Component;

@Component
public class DefectImageMapper {

    public DefectImageDto mapToDto(DefectImage defectImage) {
        DefectImageDto defectImageDto = new DefectImageDto();
        defectImageDto.setId(defectImage.getId());
        defectImageDto.setPath(defectImage.getPath());
        return defectImageDto;
    }
}
