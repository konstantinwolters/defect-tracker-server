package com.example.defecttrackerserver.core.defect.defectComment;

import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.UserMapper;
import com.example.defecttrackerserver.core.user.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Provides methods to map entity objects to DTOs and vice versa.
 */
@Component
@RequiredArgsConstructor
public class DefectCommentMapper {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public DefectComment map(DefectCommentDto defectCommentDto, DefectComment defectComment){
        User createdBy = defectCommentDto.getCreatedBy() != null ?
                userRepository.findById(defectCommentDto.getCreatedBy().getId())
                .orElseThrow(()-> new EntityNotFoundException("User not found with id: "
                        + defectCommentDto.getCreatedBy().getId())) :
                null;

        defectComment.setContent(defectCommentDto.getContent());
        defectComment.setCreatedAt(defectCommentDto.getCreatedAt());

        defectComment.setCreatedBy(createdBy);

        return defectComment;
    }

    public DefectCommentDto mapToDto(DefectComment defectComment){
        DefectCommentDto defectCommentDto = new DefectCommentDto();
        defectCommentDto.setId(defectComment.getId());
        defectCommentDto.setContent(defectComment.getContent());
        defectCommentDto.setCreatedAt(defectComment.getCreatedAt());
        defectCommentDto.setCreatedBy(userMapper.mapToDto(defectComment.getCreatedBy()));

        return defectCommentDto;
    }
}
