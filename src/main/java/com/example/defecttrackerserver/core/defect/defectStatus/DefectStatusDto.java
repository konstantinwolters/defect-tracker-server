package com.example.defecttrackerserver.core.defect.defectStatus;

import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.defect.defect.DefectDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DefectStatusDto {

    private Integer id;
    private String name;
    private List<DefectDto> defects = new ArrayList<>();

}

