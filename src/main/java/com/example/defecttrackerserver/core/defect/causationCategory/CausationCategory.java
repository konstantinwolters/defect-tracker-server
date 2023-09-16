package com.example.defecttrackerserver.core.defect.causationCategory;

import com.example.defecttrackerserver.core.defect.defect.Defect;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Entity representing metadata for {@link Defect} to assign a causation category.
 */
@Entity
@Getter
@Setter
public class CausationCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CausationCategory causationCategory = (CausationCategory) o;
        return Objects.equals(id, causationCategory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
