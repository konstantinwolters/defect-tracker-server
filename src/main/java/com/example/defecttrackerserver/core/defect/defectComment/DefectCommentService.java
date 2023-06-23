package com.example.defecttrackerserver.core.defect.defectComment;

import java.util.Optional;

public interface DefectCommentService {
    DefectCommentDto getDefectCommentById(Integer id);
    DefectCommentDto addDefectCommentToDefect(Integer defectId, DefectCommentDto defectComment);
    DefectCommentDto updateDefectComment(DefectCommentDto defectComment);
    void deleteDefectComment(Integer defectId, Integer defectCommentId);
}
