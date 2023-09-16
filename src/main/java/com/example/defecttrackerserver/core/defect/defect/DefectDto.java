package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.core.action.ActionDto;
import com.example.defecttrackerserver.core.defect.defectComment.DefectCommentDto;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImageDto;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Data transfer object for {@link Defect}.
 */
@Getter
@Setter
public class DefectDto {
    private Integer id;
    private List<DefectImageDto> images;
    private Set<DefectCommentDto> defectComments;
    private Set<ActionDto> actions;
    private String defectStatus;
    private LocalDateTime createdAt;
    private LocalDateTime changedAt;
    private UserDto changedBy;
    private String causationCategory;
    private String location;
    private String defectType;

    @NotNull(message = "Defect description must not be null.")
    @NotEmpty(message = "Defect description must not be empty.")
    private String description;

    @NotNull(message = "Lot must not be null.")
    @NotEmpty(message = "Lot must not be empty.")
    private String lot;

    @NotNull(message = "Process must not be null.")
    @NotEmpty(message = "Process must not be empty.")
    private String process;

    @NotNull(message = "CreatedBy must not be null.")
    private UserDto createdBy;
}
