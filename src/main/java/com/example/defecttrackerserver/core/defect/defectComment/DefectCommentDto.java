package com.example.defecttrackerserver.core.defect.defectComment;

import com.example.defecttrackerserver.core.user.user.dto.UserDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DefectCommentDto {

    private Integer id;
    private String content;
    private boolean defectCreator;
    private LocalDateTime createdOn;
    private UserDto createdBy;
}
