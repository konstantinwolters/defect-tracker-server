package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.user.user.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
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
    @JoinColumn(nullable = false)
    private Defect defect;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User createdBy;
}
