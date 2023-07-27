package com.example.defecttrackerserver.core.defect.defectImage;

public interface DefectImageService {
    DefectImageDto saveDefectImageToDefect(Integer defectId, DefectImageDto defectImageDto);
    DefectImageDto getDefectImageById(Integer id);
    DefectImageDto updateDefectImage(Integer defectImageId, DefectImageDto defectImageDto);
    void deleteDefectImage(Integer defectId, Integer defectImageId);
}
