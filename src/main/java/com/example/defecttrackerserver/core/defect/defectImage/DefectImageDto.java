package com.example.defecttrackerserver.core.defect.defectImage;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefectImageDto {

    private Integer id;
    private String path;
}
