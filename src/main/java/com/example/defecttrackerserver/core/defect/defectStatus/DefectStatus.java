package com.example.defecttrackerserver.core.defect.defectStatus;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
public class DefectStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefectStatus defectStatus = (DefectStatus) o;
        return Objects.equals(id, defectStatus.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

