package com.example.defecttrackerserver.core.defect.defectComment;

import com.example.defecttrackerserver.core.defect.Defect;
import com.example.defecttrackerserver.core.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class DefectComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String content;
    private boolean defectCreator;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    private Defect defect;
}
