package com.example.defecttrackerserver.core.defect.defectComment;

import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import com.example.defecttrackerserver.security.SecurityService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DefectCommentServiceImpl implements DefectCommentService {

    private final DefectCommentRepository defectCommentRepository;
    private final DefectRepository defectRepository;
    private final DefectCommentMapper defectCommentMapper;
    private final SecurityService securityService;

    @Override
    @Transactional
    public DefectCommentDto addDefectCommentToDefect(Integer defectId, DefectCommentDto defectCommentDto) {
        Defect defect = defectRepository.findById(defectId)
                .orElseThrow(() -> new EntityNotFoundException("Defect not found with id: " + defectId));

        defectCommentDto.setCreatedAt(LocalDateTime.now());

        DefectComment newDefectComment = defectCommentMapper.map(defectCommentDto, new DefectComment());
        defect.addDefectComment(newDefectComment);

        return defectCommentMapper.mapToDto(newDefectComment);
    }

    @Override
    public DefectCommentDto getDefectCommentById(Integer id) {
        DefectComment defectComment = defectCommentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DefectComment not found with id: " + id));

        return defectCommentMapper.mapToDto(defectComment);
    }

    @Override
    @Transactional
    public DefectCommentDto updateDefectComment(Integer defectCommentId, DefectCommentDto defectComment) {
        DefectComment defectCommentToUpdate = defectCommentRepository.findById(defectCommentId)
                .orElseThrow(() -> new EntityNotFoundException("DefectComment not found with id: "
                        + defectCommentId));

        if(!securityService.getUsername().equals(defectCommentToUpdate.getCreatedBy().getUsername())
        && !securityService.hasRole("ROLE_ADMIN")){
            throw new AccessDeniedException("You are not authorized to update this defect comment");
        }

        DefectComment mappedDefectComment = defectCommentMapper.map(defectComment, defectCommentToUpdate);
        DefectComment updatedDefectComment = defectCommentRepository.save(mappedDefectComment);

        return defectCommentMapper.mapToDto(updatedDefectComment);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteDefectComment(Integer defectId, Integer defectCommentId) {
        Defect defect = defectRepository.findById(defectId)
                .orElseThrow(() -> new EntityNotFoundException("Defect not found with id: " + defectId));

        DefectComment defectComment = defectCommentRepository.findById(defectCommentId)
                .orElseThrow(() -> new EntityNotFoundException("DefectComment not found with id: " + defectCommentId));

        defect.deleteDefectComment(defectComment);
    }
}
