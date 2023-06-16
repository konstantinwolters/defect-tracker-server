package com.example.defecttrackerserver.core.defect.defectComment;

import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.dto.UserDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

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
