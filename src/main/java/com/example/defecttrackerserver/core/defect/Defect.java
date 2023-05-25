package com.example.defecttrackerserver.core.defect;

import com.example.defecttrackerserver.core.Location;
import com.example.defecttrackerserver.core.defect.defectComment.DefectComment;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatus;
import com.example.defecttrackerserver.core.defect.Process.Process;
import com.example.defecttrackerserver.core.material.lot.Lot;
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

    @OneToOne(mappedBy = "defect")
    private Lot lot;

    @OneToOne(mappedBy = "defect")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    private Process process;



}
