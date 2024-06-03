package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.core.user.user.userDtos.UserInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

/**
 * Transfer object for distinct filter values for the endpoint /getFilteredActions.
 * It is part of {@link com.example.defecttrackerserver.response.PaginatedResponse}.
 */
@Getter
@Setter
class ActionFilterValues {

    /**
     * Distinct due dates for all filtered actions.
     */
    private Set<LocalDate> dueDates;

    /**
     * Distinct working status for all filtered actions.
     */
    private Set<Boolean> isCompleted;

    /**
     * Distinct assigned users for all filtered actions.
     */
    private Set<UserInfo> assignedUsers;

    /**
     * Distinct defects for all filtered actions.
     */
    private Set<Integer> defects;

    /**
     * Distinct creation dates for all filtered actions.
     */
    private Set<LocalDate> createdDates;

    /**
     * Distinct creators for all filtered actions.
     */
    private Set<UserInfo> createdByUsers;

    /**
     * Distinct changing users for all filtered actions.
     */
    private Set<UserInfo> changedByUsers;
    private Set<LocalDate> changedDates;
}
