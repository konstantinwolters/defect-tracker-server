package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.action.ActionDto;
import com.example.defecttrackerserver.core.action.ActionMapper;
import com.example.defecttrackerserver.core.action.ActionRepository;
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
import com.example.defecttrackerserver.core.user.user.UserDto;
import com.example.defecttrackerserver.core.user.user.UserMapper;
import com.example.defecttrackerserver.core.user.user.UserRepository;
import jakarta.persistence.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

        defectDto = new DefectDto();
        defectDto.setId(1);
        defectDto.setDescription("testDescription");
        defectDto.setDefectStatus("testStatus");

        defectCommentDto = new DefectCommentDto();
        defectCommentDto.setId(1);
        defectDto.setDefectComments(Set.of(defectCommentDto));

        defectDto.setLot("testLot");
        defectDto.setLocation("testCity");
        defectDto.setProcess("testProcess");
        defectDto.setDefectType("testType");

        defectImageDto = new DefectImageDto();
        defectImageDto.setId(1);
        defectDto.setImages(Set.of(defectImageDto));

        actionDto = new ActionDto();
        actionDto.setId(1);
        defectDto.setActions(Set.of(actionDto));

        userDto = new UserDto();
        userDto.setId(1);
        defectDto.setCreatedBy(userDto);
        defectDto.setCreatedAt(LocalDateTime.of(2023,2,1,0,0));
        defectDto.setChangedBy(userDto);
    }

    @Test
    void shouldReturnMappedDefect() {
        DefectStatus defectStatus = new DefectStatus();
        defectStatus.setName("testStatus");

        Lot lot = new Lot();
        lot.setId(1);
        lot.setLotNumber("testLot");

        Location location = new Location();
        location.setName("testCity");

        Process process = new Process();
        process.setName("testProcess");

        DefectType defectType = new DefectType();
        defectType.setName("testType");

        User user = new User();
        user.setId(1);

        when(defectStatusRepository.findByName(any(String.class))).thenReturn(Optional.of(defectStatus));
        when(defectCommentRepository.findById(any(Integer.class))).thenReturn(Optional.of(new DefectComment()));
        when(lotRepository.findByLotNumber(any(String.class))).thenReturn(Optional.of(lot));
        when(locationRepository.findByName(any(String.class))).thenReturn(Optional.of(location));
        when(processRepository.findByName(any(String.class))).thenReturn(Optional.of(process));
        when(defectTypeRepository.findByName(any(String.class))).thenReturn(Optional.of(defectType));
        when(defectImageRepository.findById(any(Integer.class))).thenReturn(Optional.of(new DefectImage()));
        when(actionRepository.findById(any(Integer.class))).thenReturn(Optional.of(new Action()));
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(user));

        Defect defect = new Defect();
        Defect mappedDefect = defectMapper.map(defectDto, defect);

        assertEquals(defectDto.getDefectStatus(), mappedDefect.getDefectStatus().getName());
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
        when(lotRepository.findByLotNumber(any(String.class))).thenReturn(Optional.of(new Lot()));
        when(locationRepository.findByName(any(String.class))).thenReturn(Optional.of(new Location()));
        when(processRepository.findByName(any(String.class))).thenReturn(Optional.of(new Process()));
        when(defectTypeRepository.findByName(any(String.class))).thenReturn(Optional.of(new DefectType()));
        when(defectImageRepository.findById(any(Integer.class))).thenReturn(Optional.of(new DefectImage()));
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(new User()));

        Defect defect = new Defect();
        Defect mappedDefect = defectMapper.map(defectDto, defect);

        assertNotNull(mappedDefect.getDefectComments());
        assertTrue(mappedDefect.getDefectComments().isEmpty());
    }

    @Test
    void shouldHandleNullDefectImages(){
        defectDto.setImages(null);

        when(actionRepository.findById(any(Integer.class))).thenReturn(Optional.of(new Action()));
        when(defectStatusRepository.findByName(any(String.class))).thenReturn(Optional.of(new DefectStatus()));
        when(lotRepository.findByLotNumber(any(String.class))).thenReturn(Optional.of(new Lot()));
        when(locationRepository.findByName(any(String.class))).thenReturn(Optional.of(new Location()));
        when(processRepository.findByName(any(String.class))).thenReturn(Optional.of(new Process()));
        when(defectTypeRepository.findByName(any(String.class))).thenReturn(Optional.of(new DefectType()));
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(new User()));
        when(defectCommentRepository.findById(any(Integer.class))).thenReturn(Optional.of(new DefectComment()));

        Defect defect = new Defect();
        Defect mappedDefect = defectMapper.map(defectDto, defect);

        assertNotNull(mappedDefect.getImages());
        assertTrue(mappedDefect.getImages().isEmpty());
    }

    @Test
    void shouldHandleNullActions(){
        defectDto.setActions(null);

        when(defectStatusRepository.findByName(any(String.class))).thenReturn(Optional.of(new DefectStatus()));
        when(lotRepository.findByLotNumber(any(String.class))).thenReturn(Optional.of(new Lot()));
        when(locationRepository.findByName(any(String.class))).thenReturn(Optional.of(new Location()));
        when(processRepository.findByName(any(String.class))).thenReturn(Optional.of(new Process()));
        when(defectTypeRepository.findByName(any(String.class))).thenReturn(Optional.of(new DefectType()));
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(new User()));
        when(defectCommentRepository.findById(any(Integer.class))).thenReturn(Optional.of(new DefectComment()));
        when(defectImageRepository.findById(any(Integer.class))).thenReturn(Optional.of(new DefectImage()));

        Defect defect = new Defect();
        Defect mappedDefect = defectMapper.map(defectDto, defect);

        assertNotNull(mappedDefect.getActions());
        assertTrue(mappedDefect.getActions().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenDefectStatusNotFound() {
        when(defectStatusRepository.findByName(any(String.class))).thenReturn(Optional.empty());

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
        Defect defect = new Defect();
        defect.setId(1);
        defect.setDescription("testDescription");
        defect.setCreatedAt(LocalDateTime.now());
        defect.setChangedAt(LocalDateTime.now());

        DefectStatus defectStatus = new DefectStatus();
        defectStatus.setName("testStatus");
        defect.setDefectStatus(defectStatus);

        defect.setDefectComments(Set.of(new DefectComment()));

        Lot lot = new Lot();
        lot.setLotNumber("testLot");
        defect.setLot(lot);

        Location location  = new Location();
        location.setName("testLocation");
        defect.setLocation(location);

        Process process = new Process();
        process.setName("testProcess");
        defect.setProcess(process);

        DefectType defectType = new DefectType();
        defectType.setName("testType");
        defect.setDefectType(defectType);

        defect.setImages(Set.of(new DefectImage()));
        defect.setActions(Set.of(new Action()));

        User user = new User();
        user.setId(1);
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

