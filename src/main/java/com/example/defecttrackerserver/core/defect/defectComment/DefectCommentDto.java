package com.example.defecttrackerserver.core.defect.defectComment;

import com.example.defecttrackerserver.core.user.user.UserDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DefectCommentDto {
    private Integer id;
    private LocalDateTime createdAt;

    @NotNull(message = "DefectComment content must not be null.")
    @NotEmpty(message = "DefectComment content must not be empty.")
    private String content;

    @NotNull(message = "DefectComment createdBy must not be null.")
    private UserDto createdBy;
}
