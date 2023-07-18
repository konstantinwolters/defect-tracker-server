package com.example.defecttrackerserver.core.location;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationDto {
    private Integer id;

    @NotNull(message = "Location name cannot be null")
    @NotEmpty(message = "Location name cannot be empty")
    private String name;
}
