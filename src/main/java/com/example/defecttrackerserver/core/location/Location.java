package com.example.defecttrackerserver.core.location;

import com.example.defecttrackerserver.core.defect.Defect;
import com.example.defecttrackerserver.core.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @OneToMany(
            mappedBy = "location",
            cascade = CascadeType.PERSIST)
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "location",
            cascade = CascadeType.PERSIST)
    private List<Defect> defects = new ArrayList<>();

    public void addUser(User user){
        users.add(user);
        user.setLocation(this);
    }

    public void removeUser(User user){
        users.remove(user);
        user.setLocation(null);
    }

    public void addDefect(Defect defect){
        defects.add(defect);
        defect.setLocation(this);
    }

    public void removeDefect(Defect defect){
        defects.remove(defect);
        defect.setLocation(null);
    }
}
