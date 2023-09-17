package com.example.defecttrackerserver.core.defect.defectStatus;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object for {@link DefectStatus}.
 */
@Getter
@Setter
public class DefectStatusDto {
    private Integer id;

    @NotNull(message = "DefectStatus name must not be null")
    @NotEmpty(message = "DefectStatus name must not be empty")
    private String name;
}

