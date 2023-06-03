package com.example.defecttrackerserver.core.defect.process;

import com.example.defecttrackerserver.core.defect.Defect;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Process {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @OneToMany(mappedBy = "process",
            cascade = CascadeType.PERSIST)
    private List<Defect> defects = new ArrayList<>();

    public void addDefect(Defect defect) {
        defects.add(defect);
    }

    public void removeDefect(Defect defect) {
        defects.remove(defect);
        defect.setDefectStatus(null);
    }
}