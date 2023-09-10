package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.TestHelper;
import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.action.ActionDto;
import com.example.defecttrackerserver.core.action.ActionMapper;
import com.example.defecttrackerserver.core.action.ActionRepository;
import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategory;
import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategoryRepository;
import com.example.defecttrackerserver.core.defect.defectComment.DefectComment;
import com.example.defecttrackerserver.core.defect.defectComment.DefectCommentDto;
import com.example.defecttrackerserver.core.defect.defectComment.DefectCommentMapper;
import com.example.defecttrackerserver.core.defect.defectComment.DefectCommentRepository;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImage;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImageDto;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImageMapper;
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
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.UserMapper;
import com.example.defecttrackerserver.core.user.user.UserRepository;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class DefectMapperTest {

    @Mock
    private DefectStatusRepository defectStatusRepository;

    @Mock
    private DefectCommentRepository defectCommentRepository;

    @Mock
    private LotRepository lotRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProcessRepository processRepository;

    @Mock
    private DefectTypeRepository defectTypeRepository;

    @Mock
    private ActionRepository actionRepository;

    @Mock
    private DefectImageRepository defectImageRepository;

    @Mock
    private CausationCategoryRepository causationCategoryRepository;

    @Mock
    private DefectCommentMapper defectCommentMapper;

    @Mock
    private DefectImageMapper defectImageMapper;

    @Mock
    private ActionMapper actionMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private DefectMapper defectMapper;

    DefectDto defectDto;
    UserDto userDto;
    DefectCommentDto defectCommentDto;
    DefectImageDto defectImageDto;
    ActionDto actionDto;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        defectCommentDto = TestHelper.setUpDefectCommentDto();
        defectImageDto = TestHelper.setUpDefectImageDto();
        actionDto = TestHelper.setUpActionDto();
        userDto = TestHelper.setUpUserDto();
        defectDto = TestHelper.setUpDefectDto();
        defectDto.setCreatedBy(userDto);
        defectDto.setChangedBy(userDto);
        defectDto.setActions(new HashSet<>(Set.of(actionDto)));
        defectDto.setImages(new ArrayList<>(List.of(defectImageDto)));
        defectDto.setDefectComments(new HashSet<>(Set.of(defectCommentDto)));
    }

    @Test
    void shouldReturnMappedDefect() {
        DefectStatus defectStatus = TestHelper.setUpDefectStatus();
        CausationCategory causationCategory = TestHelper.setUpCausationCategory();
        Lot lot = TestHelper.setUpLot();
        Location location = TestHelper.setUpLocation();
        Process process = TestHelper.setUpProcess();
        DefectType defectType = TestHelper.setUpDefectType();
        User user = TestHelper.setUpUser();

        when(defectStatusRepository.findByName(any(String.class))).thenReturn(Optional.of(defectStatus));
        when(causationCategoryRepository.findByName(any(String.class))).thenReturn(Optional.of(causationCategory));
        when(defectCommentRepository.findById(any(Integer.class))).thenReturn(Optional.of(new DefectComment()));
        when(lotRepository.findByLotNumber(any(String.class))).thenReturn(Optional.of(lot));
        when(locationRepository.findByName(any(String.class))).thenReturn(Optional.of(location));
        when(processRepository.findByName(any(String.class))).thenReturn(Optional.of(process));
        when(defectTypeRepository.findByName(any(String.class))).thenReturn(Optional.of(defectType));
        when(defectImageRepository.findById(any(Integer.class))).thenReturn(Optional.of(new DefectImage()));
        when(actionRepository.findById(any(Integer.class))).thenReturn(Optional.of(new Action()));
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(user));

        Defect mappedDefect = defectMapper.map(defectDto, new Defect());

        assertEquals(defectDto.getDefectStatus(), mappedDefect.getDefectStatus().getName());
        assertEquals(defectDto.getCausationCategory(), mappedDefect.getCausationCategory().getName());
        assertEquals(defectDto.getDescription(), mappedDefect.getDescription());
        assertEquals(defectDto.getDefectComments().size(), mappedDefect.getDefectComments().size());
        assertEquals(defectDto.getLot(), mappedDefect.getLot().getLotNumber());
        assertEquals(defectDto.getLocation(), mappedDefect.getLocation().getName());
        assertEquals(defectDto.getProcess(), mappedDefect.getProcess().getName());
        assertEquals(defectDto.getDefectType(), mappedDefect.getDefectType().getName());
        assertEquals(defectDto.getImages().size(), mappedDefect.getImages().size());
        assertEquals(defectDto.getActions().size(), mappedDefect.getActions().size());
        assertEquals(defectDto.getCreatedBy().getId(), mappedDefect.getCreatedBy().getId());
    }

    @Test
    void shouldHandleNullDefectComments(){
        defectDto.setDefectComments(null);

        when(actionRepository.findById(any(Integer.class))).thenReturn(Optional.of(new Action()));
        when(defectStatusRepository.findByName(any(String.class))).thenReturn(Optional.of(new DefectStatus()));
        when(causationCategoryRepository.findByName(any(String.class))).thenReturn(Optional.of(new CausationCategory()));
        when(lotRepository.findByLotNumber(any(String.class))).thenReturn(Optional.of(new Lot()));
        when(locationRepository.findByName(any(String.class))).thenReturn(Optional.of(new Location()));
        when(processRepository.findByName(any(String.class))).thenReturn(Optional.of(new Process()));
        when(defectTypeRepository.findByName(any(String.class))).thenReturn(Optional.of(new DefectType()));
        when(defectImageRepository.findById(any(Integer.class))).thenReturn(Optional.of(new DefectImage()));
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(new User()));

        Defect mappedDefect = defectMapper.map(defectDto, new Defect());

        assertNotNull(mappedDefect.getDefectComments());
        assertTrue(mappedDefect.getDefectComments().isEmpty());
    }

    @Test
    void shouldHandleNullDefectImages(){
        defectDto.setImages(null);

        when(actionRepository.findById(any(Integer.class))).thenReturn(Optional.of(new Action()));
        when(defectStatusRepository.findByName(any(String.class))).thenReturn(Optional.of(new DefectStatus()));
        when(causationCategoryRepository.findByName(any(String.class))).thenReturn(Optional.of(new CausationCategory()));
        when(lotRepository.findByLotNumber(any(String.class))).thenReturn(Optional.of(new Lot()));
        when(locationRepository.findByName(any(String.class))).thenReturn(Optional.of(new Location()));
        when(processRepository.findByName(any(String.class))).thenReturn(Optional.of(new Process()));
        when(defectTypeRepository.findByName(any(String.class))).thenReturn(Optional.of(new DefectType()));
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(new User()));
        when(defectCommentRepository.findById(any(Integer.class))).thenReturn(Optional.of(new DefectComment()));

        Defect mappedDefect = defectMapper.map(defectDto, new Defect());

        assertNotNull(mappedDefect.getImages());
        assertTrue(mappedDefect.getImages().isEmpty());
    }

    @Test
    void shouldHandleNullActions(){
        defectDto.setActions(null);

        when(defectStatusRepository.findByName(any(String.class))).thenReturn(Optional.of(new DefectStatus()));
        when(causationCategoryRepository.findByName(any(String.class))).thenReturn(Optional.of(new CausationCategory()));
        when(lotRepository.findByLotNumber(any(String.class))).thenReturn(Optional.of(new Lot()));
        when(locationRepository.findByName(any(String.class))).thenReturn(Optional.of(new Location()));
        when(processRepository.findByName(any(String.class))).thenReturn(Optional.of(new Process()));
        when(defectTypeRepository.findByName(any(String.class))).thenReturn(Optional.of(new DefectType()));
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(new User()));
        when(defectCommentRepository.findById(any(Integer.class))).thenReturn(Optional.of(new DefectComment()));
        when(defectImageRepository.findById(any(Integer.class))).thenReturn(Optional.of(new DefectImage()));

        Defect mappedDefect = defectMapper.map(defectDto, new Defect());

        assertNotNull(mappedDefect.getActions());
        assertTrue(mappedDefect.getActions().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenDefectStatusNotFound() {
        when(defectStatusRepository.findByName(any(String.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> defectMapper.map(defectDto, new Defect()));
    }

    @Test
    void shouldThrowExceptionWhenCausationCategoryNotFound() {
        when(causationCategoryRepository.findByName(any(String.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> defectMapper.map(defectDto, new Defect()));
    }

    @Test
    void shouldThrowExceptionWhenDefectCommentNotFound() {
        when(defectStatusRepository.findByName(any(String.class))).thenReturn(Optional.of(new DefectStatus()));
        when(defectCommentRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> defectMapper.map(defectDto, new Defect()));
    }

    @Test
    void shouldThrowExceptionWhenLotNotFound() {
        when(defectStatusRepository.findByName(any(String.class))).thenReturn(Optional.of(new DefectStatus()));
        when(defectCommentRepository.findById(any(Integer.class))).thenReturn(Optional.of(new DefectComment()));
        when(lotRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> defectMapper.map(defectDto, new Defect()));
    }

    @Test
    void shouldThrowExceptionWhenLocationNotFound() {
        when(defectStatusRepository.findByName(any(String.class))).thenReturn(Optional.of(new DefectStatus()));
        when(defectCommentRepository.findById(any(Integer.class))).thenReturn(Optional.of(new DefectComment()));
        when(lotRepository.findById(any(Integer.class))).thenReturn(Optional.of(new Lot()));
        when(locationRepository.findByName(any(String.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> defectMapper.map(defectDto, new Defect()));
    }

    @Test
    void shouldThrowExceptionWhenProcessNotFound() {
        when(defectStatusRepository.findByName(any(String.class))).thenReturn(Optional.of(new DefectStatus()));
        when(defectCommentRepository.findById(any(Integer.class))).thenReturn(Optional.of(new DefectComment()));
        when(lotRepository.findById(any(Integer.class))).thenReturn(Optional.of(new Lot()));
        when(locationRepository.findByName(any(String.class))).thenReturn(Optional.of(new Location()));
        when(processRepository.findByName(any(String.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> defectMapper.map(defectDto, new Defect()));
    }

    @Test
    void shouldThrowExceptionWhenDefectTypeNotFound() {
        when(defectStatusRepository.findByName(any(String.class))).thenReturn(Optional.of(new DefectStatus()));
        when(defectCommentRepository.findById(any(Integer.class))).thenReturn(Optional.of(new DefectComment()));
        when(lotRepository.findById(any(Integer.class))).thenReturn(Optional.of(new Lot()));
        when(locationRepository.findByName(any(String.class))).thenReturn(Optional.of(new Location()));
        when(processRepository.findByName(any(String.class))).thenReturn(Optional.of(new Process()));
        when(defectTypeRepository.findByName(any(String.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> defectMapper.map(defectDto, new Defect()));
    }

    @Test
    void shouldThrowExceptionWhenDefectImageNotFound() {
        when(defectStatusRepository.findByName(any(String.class))).thenReturn(Optional.of(new DefectStatus()));
        when(defectCommentRepository.findById(any(Integer.class))).thenReturn(Optional.of(new DefectComment()));
        when(lotRepository.findById(any(Integer.class))).thenReturn(Optional.of(new Lot()));
        when(locationRepository.findByName(any(String.class))).thenReturn(Optional.of(new Location()));
        when(processRepository.findByName(any(String.class))).thenReturn(Optional.of(new Process()));
        when(defectTypeRepository.findByName(any(String.class))).thenReturn(Optional.of(new DefectType()));
        when(defectImageRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> defectMapper.map(defectDto, new Defect()));
    }

    @Test
    void shouldThrowExceptionWhenActionNotFound() {
        when(defectStatusRepository.findByName(any(String.class))).thenReturn(Optional.of(new DefectStatus()));
        when(defectCommentRepository.findById(any(Integer.class))).thenReturn(Optional.of(new DefectComment()));
        when(lotRepository.findById(any(Integer.class))).thenReturn(Optional.of(new Lot()));
        when(locationRepository.findByName(any(String.class))).thenReturn(Optional.of(new Location()));
        when(processRepository.findByName(any(String.class))).thenReturn(Optional.of(new Process()));
        when(defectTypeRepository.findByName(any(String.class))).thenReturn(Optional.of(new DefectType()));
        when(defectImageRepository.findById(any(Integer.class))).thenReturn(Optional.of(new DefectImage()));
        when(actionRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> defectMapper.map(defectDto, new Defect()));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(defectStatusRepository.findByName(any(String.class))).thenReturn(Optional.of(new DefectStatus()));
        when(defectCommentRepository.findById(any(Integer.class))).thenReturn(Optional.of(new DefectComment()));
        when(lotRepository.findById(any(Integer.class))).thenReturn(Optional.of(new Lot()));
        when(locationRepository.findByName(any(String.class))).thenReturn(Optional.of(new Location()));
        when(processRepository.findByName(any(String.class))).thenReturn(Optional.of(new Process()));
        when(defectTypeRepository.findByName(any(String.class))).thenReturn(Optional.of(new DefectType()));
        when(defectImageRepository.findById(any(Integer.class))).thenReturn(Optional.of(new DefectImage()));
        when(actionRepository.findById(any(Integer.class))).thenReturn(Optional.of(new Action()));
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> defectMapper.map(defectDto, new Defect()));
    }

    @Test
    void shouldReturnMappedDefectDto() {
        DefectStatus defectStatus = TestHelper.setUpDefectStatus();
        CausationCategory causationCategory = TestHelper.setUpCausationCategory();
        Lot lot = TestHelper.setUpLot();
        Location location = TestHelper.setUpLocation();
        Process process = TestHelper.setUpProcess();
        DefectType defectType = TestHelper.setUpDefectType();
        User user = TestHelper.setUpUser();

        Defect defect = new Defect();
        defect.setDefectStatus(defectStatus);
        defect.setCausationCategory(causationCategory);
        defect.setLot(lot);
        defect.setLocation(location);
        defect.setProcess(process);
        defect.setDefectType(defectType);
        defect.setCreatedBy(user);
        defect.setChangedBy(user);

        when(defectCommentMapper.mapToDto(any())).thenReturn(new DefectCommentDto());
        when(defectImageMapper.mapToDto(any())).thenReturn(new DefectImageDto());
        when(actionMapper.mapToDto(any())).thenReturn(new ActionDto());
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userMapper.mapToDto(any())).thenReturn(userDto);

        DefectDto mappedDefectDto = defectMapper.mapToDto(defect);
        assertEquals(defect.getId(), mappedDefectDto.getId());
        assertEquals(defect.getDescription(), mappedDefectDto.getDescription());
        assertEquals(defect.getCreatedAt(), mappedDefectDto.getCreatedAt());
        assertEquals(defect.getChangedAt(), mappedDefectDto.getChangedAt());
        assertEquals(defect.getDefectStatus().getName(), mappedDefectDto.getDefectStatus());
        assertEquals(defect.getDefectComments().size(), mappedDefectDto.getDefectComments().size());
        assertEquals(defect.getLot().getLotNumber(), mappedDefectDto.getLot());
        assertEquals(defect.getLocation().getName(), mappedDefectDto.getLocation());
        assertEquals(defect.getProcess().getName(), mappedDefectDto.getProcess());
        assertEquals(defect.getDefectType().getName(), mappedDefectDto.getDefectType());
        assertEquals(defect.getImages().size(), mappedDefectDto.getImages().size());
        assertEquals(defect.getCreatedBy().getId(), mappedDefectDto.getCreatedBy().getId());
        assertEquals(defect.getChangedBy().getId(), mappedDefectDto.getChangedBy().getId());
    }
}

