package com.example.defecttrackerserver.core.user.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class UserDto  {
    private Integer id;
    private String password;
    private String firstName;
    private String lastName;
    private Set<String> roles;
    private Set<Integer> assignedActions;
    private LocalDateTime createdAt;
    private LocalDateTime changedAt;
    private Integer createdBy;
    private Integer changedBy;

    @NotNull(message = "User isActive must not be null.")
    private Boolean isActive;

    @NotNull(message = "Username must not be null.")
    @NotEmpty(message = "Username must not be empty.")
    private String username;

    @NotNull(message = "User mail must not be null.")
    @NotEmpty(message = "User mail must not be empty.")
    private String mail;

    @NotNull(message = "User location must not be null.")
    @NotEmpty(message = "User location must not be empty.")
    private String location;
}
