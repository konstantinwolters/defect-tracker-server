package com.example.defecttrackerserver.core.Action;

import com.example.defecttrackerserver.core.defect.Defect;
import com.example.defecttrackerserver.core.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;
    private LocalDate dueDate;
    private Boolean isCompleted;

    @ManyToMany(mappedBy = "assignedActions")
    private Set<User> assigned = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Defect defect;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdOn;

    @ManyToOne(fetch = FetchType.LAZY)
    private User createdBy;

}
