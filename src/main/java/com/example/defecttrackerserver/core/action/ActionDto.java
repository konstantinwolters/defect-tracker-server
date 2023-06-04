package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.core.defect.Defect;
import com.example.defecttrackerserver.core.user.User;
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
    private Set<User> assigned = new HashSet<>();
    private Defect defect;
    private LocalDateTime createdOn;
    private User createdBy;
}
