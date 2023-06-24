package com.example.defecttrackerserver.core.defect.defectComment;

import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DefectCommentServiceImpl implements DefectCommentService {

    private final DefectCommentRepository defectCommentRepository;
    private final DefectRepository defectRepository;
    private final DefectCommentMapper defectCommentMapper;
    private final ModelMapper modelMapper;

    @Override
    public DefectCommentDto getDefectCommentById(Integer id) {
        DefectComment defectComment = defectCommentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DefectComment not found with id: " + id));

        return modelMapper.map(defectComment, DefectCommentDto.class);
    }

    @Override
    @Transactional
    public DefectCommentDto addDefectCommentToDefect(Integer defectId, DefectCommentDto defectCommentDto) {
        Defect defect = defectRepository.findById(defectId)
                .orElseThrow(() -> new EntityNotFoundException("Defect not found with id: " + defectId));

        defectCommentDto.setCreatedOn(LocalDateTime.now());
        defectCommentMapper.checkNullOrEmptyFields(defectCommentDto);

        DefectComment defectComment = new DefectComment();
        DefectComment newDefectComment = defectCommentMapper.map(defectCommentDto, defectComment);
        defect.addDefectComment(newDefectComment);
        defectRepository.save(defect);

        return modelMapper.map(newDefectComment, DefectCommentDto.class);
    }

    @Override
    public DefectCommentDto updateDefectComment(DefectCommentDto defectComment) {
        DefectComment defectCommentToUpdate = defectCommentRepository.findById(defectComment.getId())
                .orElseThrow(() -> new EntityNotFoundException("DefectComment not found with id: "
                        + defectComment.getId()));

        defectCommentMapper.checkNullOrEmptyFields(defectComment);

        DefectComment mappedDefectComment = defectCommentMapper.map(defectComment, defectCommentToUpdate);
        DefectComment updatedDefectComment = defectCommentRepository.save(mappedDefectComment);

        return modelMapper.map(updatedDefectComment, DefectCommentDto.class);
    }

    @Override
    @Transactional
    public void deleteDefectComment(Integer defectId, Integer defectCommentId) {
        Defect defect = defectRepository.findById(defectId)
                .orElseThrow(() -> new EntityNotFoundException("Defect not found with id: " + defectId));

        DefectComment defectComment = defectCommentRepository.findById(defectCommentId)
                .orElseThrow(() -> new EntityNotFoundException("DefectComment not found with id: " + defectCommentId));

        defect.deleteDefectComment(defectComment);
        defectRepository.save(defect);
    }
}
