package com.example.defecttrackerserver.core.defect.defectImage;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object for {@link DefectImage}.
 */
@Getter
@Setter
public class DefectImageDto {
    private Integer id;

    @NotNull(message = "Image path must not be null")
    @NotEmpty(message = "Image path must not be empty")
    private String path;
}
