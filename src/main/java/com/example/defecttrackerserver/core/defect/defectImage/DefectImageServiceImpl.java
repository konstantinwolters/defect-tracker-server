package com.example.defecttrackerserver.core.defect.defectImage;

import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import com.example.defecttrackerserver.utils.Utils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * Implementation of {@link DefectImageService}.
 */
@Service
@RequiredArgsConstructor
public class DefectImageServiceImpl implements DefectImageService{
    private final DefectRepository defectRepository;
    private final DefectImageRepository defectImageRepository;
    private final Utils utils;
    private final DefectImageMapper defectImageMapper;

    @Override
    @Transactional
    public DefectImageDto saveDefectImageToDefect(Integer defectId, MultipartFile image) {
        Defect defect = defectRepository.findById(defectId)
                .orElseThrow(()-> new EntityNotFoundException("Defect not found with id: " + defectId));

        String imageUuid = utils.uploadImage(image);

        DefectImage defectImage = new DefectImage();
        defectImage.setUuid(imageUuid);
        defect.addDefectImage(defectImage);

        return defectImageMapper.mapToDto(defectImage);
    }

    @Override
    public DefectImageDto getDefectImageById(Integer id) {
        DefectImage defectImage =  defectImageRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("DefectImage not found with id: " + id));
        return defectImageMapper.mapToDto(defectImage);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteDefectImage(Integer defectId, Integer defectImageId) {
        Defect defect = defectRepository.findById(defectId)
                .orElseThrow(()-> new EntityNotFoundException("Defect not found with id: " + defectId));

        DefectImage defectImage = defectImageRepository.findById(defectImageId)
                .orElseThrow(()-> new EntityNotFoundException("DefectImage not found with id: " + defectImageId));

        utils.removeImage(defectImage.getUuid());
        defect.deleteDefectImage(defectImage);
    }
}
