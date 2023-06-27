package com.example.defecttrackerserver.core.defect.defectImage;

public interface DefectImageService {
    DefectImageDto saveDefectImageToDefect(Integer defectId, DefectImageDto defectImageDto);
    DefectImageDto getDefectImageById(Integer id);
    DefectImageDto updateDefectImage(DefectImageDto defectImageDto);
    void deleteDefectImageFromDefect(Integer defectId, Integer defectImageId);
}
