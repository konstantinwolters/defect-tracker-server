package com.example.defecttrackerserver.core.defect.defectType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefectTypeDto {

    private Integer id;
    private String name;
}
