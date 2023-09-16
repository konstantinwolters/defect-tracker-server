package com.example.defecttrackerserver.core.defect.defectComment;

import com.example.defecttrackerserver.core.user.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entity representing a comment item that can be added to a defect.
 *
 * @author Konstantin Wolters
 */
@Entity
@Getter
@Setter
public class DefectComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String content;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    private User createdBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefectComment defectComment = (DefectComment) o;
        return Objects.equals(id, defectComment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
