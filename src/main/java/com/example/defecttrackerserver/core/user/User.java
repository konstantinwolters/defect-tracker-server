package com.example.defecttrackerserver.core.user;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.defect.Defect;
import com.example.defecttrackerserver.core.defect.defectComment.DefectComment;
import com.example.defecttrackerserver.core.user.role.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String username;
    private String firstName;
    private String lastName;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String mail;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    private Location location;

    @OneToMany(
            mappedBy = "createdBy",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<DefectComment> defectComments = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy",
            cascade = CascadeType.PERSIST)
    private List<Defect> defects = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_actions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "action_id")
    )
    private Set<Action> assignedActions = new HashSet<>();

    @OneToMany(
            mappedBy = "createdBy",
            cascade = CascadeType.PERSIST)
    private List<Action> createdActions = new ArrayList<>();

    public void addRole(Role role){
        roles.add(role);
        role.getUser().add(this);
    }

    public void removeRole(Role role){
        roles.remove(role);
        role.getUser().remove(this);
    }

    public void addDefectComment(DefectComment defectComment){
        defectComments.add(defectComment);
        defectComment.setCreatedBy(this);
    }

    public void removeDefectComment(DefectComment defectComment){
        defectComments.remove(defectComment);
        defectComment.setCreatedBy(null);
    }

    public void addAssignedAction(Action action){
        assignedActions.add(action);
        action.getAssigned().add(this);
    }

    public void removeAssignedAction(Action action){
        assignedActions.remove(action);
        action.getAssigned().remove(this);
    }

    public void addCreatedAction(Action action){
        createdActions.add(action);
        action.setCreatedBy(this);
    }

    public void removeCreatedAction(Action action){
        createdActions.remove(action);
        action.setCreatedBy(null);
    }

    public void addDefect(Defect defect){
        defects.add(defect);
        defect.setCreatedBy(this);
    }

    public void removeDefect(Defect defect){
        defects.remove(defect);
        defect.setCreatedBy(null);
    }
}
