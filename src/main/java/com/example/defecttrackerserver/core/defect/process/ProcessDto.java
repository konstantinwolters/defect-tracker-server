package com.example.defecttrackerserver.core.defect.process;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessDto {

    private Integer id;
    private String name;
}
