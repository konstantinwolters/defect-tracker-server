package com.example.defecttrackerserver.core.defect.defectComment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefectCommentServiceImpl {

    private final DefectCommentRepository defectCommentRepository;

    public DefectComment save(DefectComment defectComment) {
        return defectCommentRepository.save(defectComment);
    }
}
