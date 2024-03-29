package com.example.defecttrackerserver.core.coreService;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.action.ActionRepository;
import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategory;
import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategoryRepository;
import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import com.example.defecttrackerserver.core.defect.defectComment.DefectComment;
import com.example.defecttrackerserver.core.defect.defectComment.DefectCommentRepository;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImage;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImageRepository;
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
import com.example.defecttrackerserver.core.user.role.RoleRepository;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service for fetching entities from repository layer.
 */
@Service
@RequiredArgsConstructor
public class EntityService {

    private final UserRepository userRepository;
    private final ActionRepository actionRepository;
    private final DefectImageRepository defectImageRepository;
    private final DefectTypeRepository defectTypeRepository;
    private final ProcessRepository processRepository;
    private final DefectStatusRepository defectStatusRepository;
    private final CausationCategoryRepository causationCategoryRepository;
    private final DefectCommentRepository defectCommentRepository;
    private final LotRepository lotRepository;
    private final LocationRepository locationRepository;
    private final DefectRepository defectRepository;
    private final RoleRepository roleRepository;
    private final MaterialRepository materialRepository;
    private final SupplierRepository supplierRepository;

    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with name: " + username));
    }

    public Action getActionById(Integer id) {
        return actionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Action not found with id: " + id));
    }

    public DefectImage getDefectImageById(Integer id) {
        return defectImageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Defect image not found with id: " + id));
    }

    public DefectType getDefectTypeByName(String name) {
        return defectTypeRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Defect type not found with name: "
                        + name));
    }

    public Process getProcessByName(String name) {
        return processRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Process not found with name: "
                        + name));
    }

    public DefectStatus getDefectStatusByName(String name) {
        return defectStatusRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Defect status not found with name: "
                        + name));
    }

    public CausationCategory getCausationCategoryByName(String name) {
        return causationCategoryRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Causation category not found with name: "
                        + name));
    }

    public DefectComment getDefectCommentById(Integer id) {
        return defectCommentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Defect comment not found with id: " + id));
    }

    public Lot getLotByLotNumber(String lotNumber) {
        return lotRepository.findByLotNumber(lotNumber)
                .orElseThrow(() -> new EntityNotFoundException("Lot not found with number: "
                        + lotNumber));
    }

    public Location getLocationByName(String name) {
        return locationRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with name: "
                        + name));
    }

    public Defect getDefectById(Integer id) {
        return defectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Defect not found with id: " + id));
    }

    public Role getRoleByName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with name: "
                        + roleName));
    }

    public Material getMaterialById(Integer id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Material not found with id:" + id));
    }

    public Supplier getSupplierById(Integer id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + id));
    }

    public Lot getLotById(Integer id) {
        return lotRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Lot not found with id: " + id));
    }
}

