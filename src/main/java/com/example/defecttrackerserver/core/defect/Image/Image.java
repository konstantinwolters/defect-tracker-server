package com.example.defecttrackerserver.core.defect.Image;

import com.example.defecttrackerserver.core.defect.Defect;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    private Defect defect;
}
