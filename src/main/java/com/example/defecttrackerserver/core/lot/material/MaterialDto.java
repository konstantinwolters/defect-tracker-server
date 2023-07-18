package com.example.defecttrackerserver.core.lot.material;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialDto {
    private Integer id;

    @NotNull(message = "Material name cannot be null")
    @NotEmpty(message = "Material name cannot be empty")
    private String name;
}
