package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.core.user.user.userDtos.UserInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class ActionFilterValues {
    private Set<LocalDate> dueDates;
    private Set<Boolean> isCompleted;
    private Set<UserInfo> assignedUsers;
    private Set<Integer> defects;
    private Set<LocalDate> createdDates;
    private Set<UserInfo> createdByUsers;
    private Set<UserInfo> changedByUsers;
    private Set<LocalDate> changedDates;
}
