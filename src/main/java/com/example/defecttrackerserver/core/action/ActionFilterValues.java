package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.core.user.user.UserInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class ActionFilterValues {
    private Set<LocalDate> dueDate;
    private Set<Boolean> isCompleted;
    private Set<UserInfo> assignedUsers;
    private Set<Integer> defect;
    private Set<LocalDate> createdOn;
    private Set<UserInfo> createdBy;
}
