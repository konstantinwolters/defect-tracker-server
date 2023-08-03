package com.example.defecttrackerserver.core.defect.defectImage;

import com.example.defecttrackerserver.core.defect.defectComment.DefectComment;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
public class DefectImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String path;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefectImage defectImage = (DefectImage) o;
        return Objects.equals(id, defectImage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }}
