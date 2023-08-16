package com.example.defecttrackerserver.core.defect.defectImage;

import org.springframework.web.multipart.MultipartFile;

public interface DefectImageService {
    DefectImageDto saveDefectImageToDefect(Integer defectId, MultipartFile image);
    DefectImageDto getDefectImageById(Integer id);
    void deleteDefectImage(Integer defectId, Integer defectImageId);
}
