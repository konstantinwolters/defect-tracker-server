package com.example.defecttrackerserver.core.user.role;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object for {@link Role}.
 */
@Getter
@Setter
public class RoleDto {
    private Integer id;

    @NotNull(message = "Role name is required")
    @NotEmpty(message = "Role name is required")
    private String name;
}
