package com.example.defecttrackerserver.core.user;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.action.ActionRepository;
import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import com.example.defecttrackerserver.core.defect.defectComment.DefectComment;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatus;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusRepository;
import com.example.defecttrackerserver.core.defect.defectType.DefectType;
import com.example.defecttrackerserver.core.defect.defectType.DefectTypeRepository;
import com.example.defecttrackerserver.core.defect.process.Process;
import com.example.defecttrackerserver.core.defect.process.ProcessRepository;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.location.LocationRepository;
import com.example.defecttrackerserver.core.lot.lot.Lot;
import com.example.defecttrackerserver.core.lot.lot.LotRepository;
import com.example.defecttrackerserver.core.lot.material.Material;
import com.example.defecttrackerserver.core.lot.material.MaterialRepository;
import com.example.defecttrackerserver.core.lot.supplier.Supplier;
import com.example.defecttrackerserver.core.lot.supplier.SupplierRepository;
import com.example.defecttrackerserver.core.user.role.Role;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

//Just for Testing-Purposes. Gets removed later
@Component
@RequiredArgsConstructor
public class UserCreationStartup implements ApplicationRunner {

    private final LocationRepository locationRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ActionRepository actionRepository;
    private final DefectRepository defectRepository;
    private final DefectStatusRepository defectStatusRepository;
    private final DefectTypeRepository defectTypeRepository;
    private final LotRepository lotRepository;
    private final ProcessRepository processRepository;
    private final MaterialRepository materialRepository;
    private final SupplierRepository supplierRepository;

    @Override
    public void run(ApplicationArguments args) {

        Role role = new Role();
        role.setName("ROLE_ADMIN");

        Location location = new Location();
        location.setName("Texas");
        locationRepository.save(location);

        User user = new User();
        user.setUsername("bill");
        user.setPassword(passwordEncoder.encode("12345"));
        user.setMail("test@test.de");
        user.setLocation(location);
        user.addRole(role);
        user.setLocation(location);

        Action action = new Action();
        action.setDescription("Test Action");
        action.setIsCompleted(false);
        action.setDueDate(LocalDate.now());
        action.setCreatedOn(LocalDateTime.now());
        action.setCreatedBy(user);

        DefectStatus defectStatus = new DefectStatus();
        defectStatus.setName("Test Status");
        defectStatusRepository.save(defectStatus);

        DefectType defectType = new DefectType();
        defectType.setName("Test Type");
        defectTypeRepository.save(defectType);


        Process process = new Process();
        process.setName("Test Process");
        processRepository.save(process);

        Lot lot = new Lot();

        Material material  = new Material();
        material.setName("Chocolate");
        lot.setMaterial(materialRepository.save(material));

        Supplier supplier = new Supplier();
        supplier.setName("Lekkerland");
        lot.setSupplier(supplierRepository.save(supplier));

        lotRepository.save(lot);

        DefectComment defectComment = new DefectComment();
        defectComment.setContent("Test Comment");
        defectComment.setCreatedOn(LocalDateTime.now());
        defectComment.setCreatedBy(user);

        Defect defect = new Defect();
        defect.setDefectStatus(defectStatus);
        defect.addDefectComment(defectComment);
        defect.setDefectType(defectType);
        defect.setLot(lot);
        defect.setLocation(location);
        defect.setCreatedOn(LocalDateTime.now());
        defect.setCreatedBy(user);
        defect.setProcess(process);

        User savedUser = userRepository.save(user);

        Defect savedDefect = defectRepository.save(defect);
        action.setDefect(defect);
        actionRepository.save(action);

        savedUser.addAssignedAction(action);
        savedDefect.addAction(action);
        userRepository.save(savedUser);
        defectRepository.save(savedDefect);
    }
}
