package com.example.defecttrackerserver.core.user.user;

import com.example.defecttrackerserver.core.location.LocationDto;
import com.example.defecttrackerserver.core.user.role.RoleDto;
import com.example.defecttrackerserver.core.user.user.userDtos.UserInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

/**
 * Transfer object for distinct filter values for the endpoint /getFilteredUsers
 * It is part of {@link com.example.defecttrackerserver.response.PaginatedResponse}.
 */
@Getter
@Setter
public class UserFilterValues {
    private Set<Boolean> isActive;
    private Set<LocationDto> locations;
    private Set<RoleDto> roles;
    private Set<LocalDate> createdDates;
    private Set<UserInfo> createdByUsers;
    private Set<UserInfo> changedByUsers;
    private Set<LocalDate> changedDates;
}
