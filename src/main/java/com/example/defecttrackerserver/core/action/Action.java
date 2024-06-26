package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.user.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Entity representing an action item.
 *
 * @author Konstantin Wolters
 */
@Entity
@Getter
@Setter
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)
    private Boolean isCompleted;

    @ManyToMany(mappedBy = "assignedActions")
    private Set<User> assignedUsers = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Defect defect;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime changedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    private User changedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return Objects.equals(id, action.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
