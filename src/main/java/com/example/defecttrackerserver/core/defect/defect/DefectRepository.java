package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.core.user.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface DefectRepository extends JpaRepository<Defect, Integer>, JpaSpecificationExecutor<Defect> {
}
