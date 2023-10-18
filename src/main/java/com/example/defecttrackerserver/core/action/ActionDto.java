package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Data transfer object for {@link Action}.
 */
@Getter
@Setter
public class ActionDto {
    private Integer id;
    private Boolean isCompleted;
    private LocalDateTime createdAt;
    private LocalDateTime changedAt;
    private UserDto changedBy;
    private Integer defect;

    @NotNull(message = "Action description must not be null.")
    @NotEmpty(message = "Action description must not be empty.")
    private String description;

    @NotNull(message = "Action dueDate must not be null.")
    private LocalDate dueDate;

    @NotNull(message = "Action assignedUsers must not be null.")
    @NotEmpty(message = "Action assignedUsers must not be empty.")
    private Set<UserDto> assignedUsers;

    @NotNull(message = "Action createdBy must not be null.")
    private UserDto createdBy;
}
