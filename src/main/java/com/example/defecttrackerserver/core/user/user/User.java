package com.example.defecttrackerserver.core.user.user;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.user.role.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Entity representing a user.
 */
@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String customId;

    @Column(unique = true, nullable = false)
    private String username;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private LocalDateTime changedAt;
    private Integer createdById;
    private Integer changedById;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String mail;

    @ManyToOne(fetch = FetchType.LAZY)
    private Location location;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_actions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "action_id")
    )
    private Set<Action> assignedActions = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void addRole(Role role){
        roles.add(role);
    }

    public void removeRole(Role role){
        roles.remove(role);
    }

    public void addAssignedAction(Action action){
        assignedActions.add(action);
        action.getAssignedUsers().add(this);
    }

    public void removeAssignedAction(Action action){
        assignedActions.remove(action);
        action.getAssignedUsers().remove(this);
    }
}
