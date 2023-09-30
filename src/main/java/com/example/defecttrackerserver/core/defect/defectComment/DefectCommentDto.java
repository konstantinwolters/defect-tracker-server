package com.example.defecttrackerserver.core.defect.defectComment;

import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Data transfer object for {@link DefectComment}.
 */
@Getter
@Setter
public class DefectCommentDto {
    private Integer id;
    private LocalDateTime createdAt;
    private UserDto createdBy;

    @NotNull(message = "DefectComment content must not be null.")
    @NotEmpty(message = "DefectComment content must not be empty.")
    private String content;
}
