package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.core.defect.DefectDto;
import com.example.defecttrackerserver.core.user.UserDto;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

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
