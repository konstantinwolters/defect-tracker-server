package com.example.defecttrackerserver.core.defect;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.defect.defectType.DefectType;
import com.example.defecttrackerserver.core.defect.defectImage.defectImage;
import com.example.defecttrackerserver.core.defect.defectComment.DefectComment;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatus;
import com.example.defecttrackerserver.core.defect.process.Process;
import com.example.defecttrackerserver.core.material.lot.Lot;
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
public class Defect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private DefectStatus defectStatus;

    @OneToMany(mappedBy = "defect",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<DefectComment> defectComments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Lot lot;

    @ManyToOne(fetch = FetchType.LAZY)
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    private Process process;

    @ManyToOne(fetch = FetchType.LAZY)
    private DefectType defectType;

    @OneToMany(mappedBy = "defect",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<defectImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "defect",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Action> actions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private User createdBy;

    public void addDefectComment(DefectComment defectComment) {
        defectComments.add(defectComment);
        defectComment.setDefect(this);
    }

     public void deleteDefectComment(DefectComment defectComment) {
         defectComments.remove(defectComment);
         defectComment.setDefect(null);
     }

     public void addDefectImage(defectImage image) {
         images.add(image);
         image.setDefect(this);
     }

     public void deleteDefectImage(defectImage image) {
         images.remove(image);
         image.setDefect(null);
     }

     public void addAction(Action action) {
         actions.add(action);
         action.setDefect(this);
     }

     public void deleteAction(Action action) {
         actions.remove(action);
         action.setDefect(null);
     }
}
