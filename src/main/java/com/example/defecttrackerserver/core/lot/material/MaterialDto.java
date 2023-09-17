package com.example.defecttrackerserver.core.lot.material;

import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * Data transfer object for {@link Material}.
 */
@Getter
@Setter
public class MaterialDto {
    private Integer id;
    private String customId;

    @NotNull(message = "Material name cannot be null")
    @NotEmpty(message = "Material name cannot be empty")
    private String name;

    @NotNull
    @NotEmpty
    private Set<UserDto> responsibleUsers;

}
