package com.example.defecttrackerserver.core.coreService;

import com.example.defecttrackerserver.core.action.ActionRepository;
import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategoryRepository;
import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import com.example.defecttrackerserver.core.defect.defectComment.DefectCommentRepository;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImageRepository;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusRepository;
import com.example.defecttrackerserver.core.defect.defectType.DefectTypeRepository;
import com.example.defecttrackerserver.core.defect.process.ProcessRepository;
import com.example.defecttrackerserver.core.location.LocationRepository;
import com.example.defecttrackerserver.core.lot.lot.LotRepository;
import com.example.defecttrackerserver.core.lot.material.MaterialRepository;
import com.example.defecttrackerserver.core.lot.supplier.SupplierRepository;
import com.example.defecttrackerserver.core.user.role.RoleRepository;
import com.example.defecttrackerserver.core.user.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


class EntityServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ActionRepository actionRepository;

    @Mock
    private DefectRepository defectRepository;

    @Mock
    private DefectImageRepository defectImageRepository;

    @Mock
    private DefectCommentRepository defectCommentRepository;

    @Mock
    private CausationCategoryRepository causationCategoryRepository;

    @Mock
    private DefectStatusRepository defectStatusRepository;

    @Mock
    private DefectTypeRepository defectTypeRepository;

    @Mock
    private ProcessRepository processRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private LotRepository lotRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private EntityService entityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundById() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> entityService.getUserById(1));
    }

    @Test
    void shouldThrowExceptionWhenActionNotFoundById() {
        when(actionRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> entityService.getActionById(1));
    }

    @Test
    void shouldThrowExceptionWhenDefectImageNotFoundById() {
        when(defectImageRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> entityService.getDefectImageById(1));
    }

    @Test
    void shouldThrowExceptionWhenDefectTypeNotFoundByName() {
        when(defectTypeRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> entityService.getDefectTypeByName("testDefectType"));
    }

    @Test
    void shouldThrowExceptionWhenProcessNotFoundByName() {
        when(processRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> entityService.getProcessByName("testProcess"));
    }

    @Test
    void shouldThrowExceptionWhenDefectStatusNotFoundByName() {
        when(defectStatusRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> entityService.getDefectStatusByName("testDefectStatus"));
    }

    @Test
    void shouldThrowExceptionWhenCausationCategoryNotFoundByName() {
        when(causationCategoryRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> entityService.getCausationCategoryByName("testCausationCategory"));
    }

    @Test
    void shouldThrowExceptionWhenDefectCommentNotFoundById() {
        when(defectCommentRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> entityService.getDefectCommentById(1));
    }

    @Test
    void shouldThrowExceptionWhenLotNotFoundByLotNumber() {
        when(lotRepository.findByLotNumber(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> entityService.getLotByLotNumber("testLotNumber"));
    }

    @Test
    void shouldThrowExceptionWhenLocationNotFoundByName() {
        when(locationRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> entityService.getLocationByName("testLocation"));
    }

    @Test
    void shouldThrowExceptionWhenDefectNotFoundById() {
        when(defectRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> entityService.getDefectById(1));
    }

    @Test
    void shouldThrowExceptionWhenRoleNotFoundByName() {
        when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> entityService.getRoleByName("testRole"));
    }
}