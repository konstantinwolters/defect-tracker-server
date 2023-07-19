package com.example.defecttrackerserver.core.defect.defectComment;

public interface DefectCommentService {
    DefectCommentDto addDefectCommentToDefect(Integer defectId, DefectCommentDto defectComment);
    DefectCommentDto getDefectCommentById(Integer id);

    DefectCommentDto updateDefectComment(Integer defectCommentId, DefectCommentDto defectComment);
    void deleteDefectComment(Integer defectId, Integer defectCommentId);
}
