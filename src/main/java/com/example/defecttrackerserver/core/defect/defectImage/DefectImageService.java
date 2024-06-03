package com.example.defecttrackerserver.core.defect.defectImage;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service for managing {@link DefectImage}.
 */
interface DefectImageService {
    DefectImageDto saveDefectImageToDefect(Integer defectId, MultipartFile image);
    DefectImageDto getDefectImageById(Integer id);
    String getDefectImageUrl(String uuid);
    List<String> getDefectImageUrls(Integer defectId);
    void deleteDefectImage(Integer defectId, Integer defectImageId);
}
