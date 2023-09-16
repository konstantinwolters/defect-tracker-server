package com.example.defecttrackerserver.core.defect.causationCategory;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object for {@link CausationCategory}.
 */
@Getter
@Setter
public class CausationCategoryDto {
    private Integer id;

    @NotNull(message = "CausationCategory name must not be null")
    @NotEmpty(message = "CausationCategory name must not be empty")
    private String name;
}
