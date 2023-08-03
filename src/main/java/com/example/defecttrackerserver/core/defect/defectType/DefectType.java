package com.example.defecttrackerserver.core.defect.defectType;

import com.example.defecttrackerserver.core.defect.defectComment.DefectComment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
public class DefectType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefectType defectType = (DefectType) o;
        return Objects.equals(id, defectType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
