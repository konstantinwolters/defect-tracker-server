package com.example.defecttrackerserver.core.defect.process;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object for {@link Process}.
 */
@Getter
@Setter
public class ProcessDto {
    private Integer id;
    private String customId;

    @NotNull(message = "Process name must not be null")
    @NotEmpty(message = "Process name must not be empty")
    private String name;
}
