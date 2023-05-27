package com.example.defecttrackerserver.core.defect.defectType;

import com.example.defecttrackerserver.core.defect.Defect;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class DefectType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @OneToMany(mappedBy = "defectType",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Defect> defects = new ArrayList<>();

    public void addDefect(Defect defect) {
        defects.add(defect);
    }

    public void removeDefect(Defect defect) {
        defects.remove(defect);
        defect.setDefectStatus(null);
    }
}
