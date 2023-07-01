package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.defect.defectComment.DefectComment;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImage;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatus;
import com.example.defecttrackerserver.core.defect.defectType.DefectType;
import com.example.defecttrackerserver.core.defect.process.Process;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.lot.lot.Lot;
import com.example.defecttrackerserver.core.user.user.User;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Defect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime createdOn;

    @ManyToOne
    @JoinColumn(name = "defect_status_id", nullable = false)
    private DefectStatus defectStatus;

    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "defect_id")
    private Set<DefectComment> defectComments = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "lot_id", nullable = false)
    private Lot lot;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @ManyToOne
    @JoinColumn(name = "process_id", nullable = false)
    private Process process;

    @ManyToOne
    @JoinColumn(name = "defect_type_id", nullable = false)
    private DefectType defectType;

    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "defect_id")
    private Set<DefectImage> images = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "defect_id")
    private Set<Action> actions = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "created_by_id", nullable = false)
    private User createdBy;

    public void addDefectComment(DefectComment defectComment) {
        defectComments.add(defectComment);
    }

     public void deleteDefectComment(DefectComment defectComment) {
         defectComments.remove(defectComment);
     }

     public void addDefectImage(DefectImage image) {
         images.add(image);
     }

     public void deleteDefectImage(DefectImage image) {
         images.remove(image);
     }

     public void addAction(Action action) {
         actions.add(action);
         action.setDefect(this);
     }

     public void deleteAction(Action action) {
        action.getAssignedUsers().forEach(user -> user.removeAssignedAction(action));
        actions.remove(action);
        action.setDefect(null);
     }
}
