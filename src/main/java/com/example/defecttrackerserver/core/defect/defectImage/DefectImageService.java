package com.example.defecttrackerserver.core.defect.defectImage;

public interface DefectImageService {
    DefectImageDto saveDefectImageToDefect(Integer defectId, DefectImageDto defectImageDto);
    DefectImageDto getDefectImageById(Integer id);
    void deleteDefectImage(Integer defectId, Integer defectImageId);
}
