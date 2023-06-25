package com.example.defecttrackerserver.core.defect.defectComment;

import com.example.defecttrackerserver.core.user.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefectCommentMapper {
    private final UserRepository userRepository;

    public DefectComment map(DefectCommentDto defectCommentDto, DefectComment defectComment){
        checkNullOrEmptyFields(defectCommentDto);

        defectComment.setContent(defectCommentDto.getContent());
        defectComment.setCreatedOn(defectCommentDto.getCreatedOn());

        defectComment.setCreatedBy(userRepository.findById(defectCommentDto.getCreatedBy().getId())
                .orElseThrow(()-> new EntityNotFoundException("User not found with id: "
                        + defectCommentDto.getCreatedBy().getId())));

        return defectComment;
    }

    public void checkNullOrEmptyFields(DefectCommentDto defectCommentDto) {
        if(defectCommentDto.getContent() == null || defectCommentDto.getContent().isEmpty())
            throw new IllegalArgumentException("Description must not be null or empty");
        if(defectCommentDto.getCreatedOn() == null)
            throw  new IllegalArgumentException("Created on must not be null");
        if(defectCommentDto.getCreatedBy() == null)
            throw new IllegalArgumentException("Created by must not be null");
    }
}
