package com.example.defecttrackerserver.core.defect.defectComment;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface DefectCommentRepository extends JpaRepository<DefectComment, Integer> {

    Set<DefectComment> findByCreatedById(Integer id);
}
