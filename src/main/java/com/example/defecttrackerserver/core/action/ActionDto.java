package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.core.defect.defect.DefectDto;
import com.example.defecttrackerserver.core.user.user.dto.UserDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ActionDto {
    private Integer id;
    private String description;
    private Boolean isCompleted;
    private LocalDate dueDate;
    private Set<UserDto> assignedUsers = new HashSet<>();
    private DefectDto defect;
    private LocalDateTime createdOn;
    private UserDto createdBy;
}
