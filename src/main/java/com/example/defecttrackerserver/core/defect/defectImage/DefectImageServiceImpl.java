package com.example.defecttrackerserver.core.defect.defectImage;

import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefectImageServiceImpl implements DefectImageService{
    private final DefectRepository defectRepository;
    private final DefectImageRepository defectImageRepository;
    private final ModelMapper modelMapper;

    @Override
    public DefectImageDto saveDefectImageToDefect(Integer defectId, DefectImageDto defectImageDto) {
        Defect defect = defectRepository.findById(defectId)
                .orElseThrow(()-> new EntityNotFoundException("Defect not found with id: " + defectId));

        if(defectImageDto.getPath() == null) {
            throw new IllegalArgumentException("Path must not be null");
        }

        DefectImage defectImage = new DefectImage();
        defectImage.setPath(defectImageDto.getPath());
        defect.addDefectImage(defectImage);
        Defect savedDefect = defectRepository.save(defect);

        return modelMapper.map(savedDefect, DefectImageDto.class);
    }

    @Override
    public DefectImageDto getDefectImageById(Integer id) {
        DefectImage defectImage =  defectImageRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("DefectImage not found with id: " + id));
        return modelMapper.map(defectImage, DefectImageDto.class);
    }

    @Override
    public DefectImageDto updateDefectImage(DefectImageDto defectImageDto) {
        DefectImage defectImage =  defectImageRepository.findById(defectImageDto.getId())
                .orElseThrow(()-> new EntityNotFoundException("DefectImage not found with id: "
                        + defectImageDto.getId()));

        if (defectImageDto.getPath() == null) {
            throw new IllegalArgumentException("Path must not be null");
        }

        defectImage.setPath(defectImageDto.getPath());
        DefectImage savedDefectImage = defectImageRepository.save(defectImage);

        return modelMapper.map(savedDefectImage, DefectImageDto.class);
    }

    @Override
    public void deleteDefectImageById(Integer defectId, Integer defectImageId) {
        Defect defect = defectRepository.findById(defectId)
                .orElseThrow(()-> new EntityNotFoundException("Defect not found with id: " + defectId));

        DefectImage defectImage = defectImageRepository.findById(defectImageId)
                .orElseThrow(()-> new EntityNotFoundException("DefectImage not found with id: " + defectImageId));

        defect.deleteDefectImage(defectImage);
        defectImageRepository.delete(defectImage);
    }
}
