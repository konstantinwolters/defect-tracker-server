package com.example.defecttrackerserver.core.defect.defectType;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefectTypeDto {
    private Integer id;

    @NotNull(message = "DefectType name must not be null")
    @NotEmpty(message = "DefectType name must not be empty")
    private String name;
}
