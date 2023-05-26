package com.example.defecttrackerserver.core.defect;

import com.example.defecttrackerserver.core.Action.Action;
import com.example.defecttrackerserver.core.Location;
import com.example.defecttrackerserver.core.defect.DefectType.DefectType;
import com.example.defecttrackerserver.core.defect.Image.Image;
import com.example.defecttrackerserver.core.defect.defectComment.DefectComment;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatus;
import com.example.defecttrackerserver.core.defect.Process.Process;
import com.example.defecttrackerserver.core.material.lot.Lot;
import com.example.defecttrackerserver.core.user.User;
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
    private List<Image> images = new ArrayList<>();

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

     public void addImage(Image image) {
         images.add(image);
         image.setDefect(this);
     }

     public void deleteImage(Image image) {
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
