package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.action.ActionDto;
import com.example.defecttrackerserver.core.action.ActionRepository;
import com.example.defecttrackerserver.core.defect.defectComment.DefectComment;
import com.example.defecttrackerserver.core.defect.defectComment.DefectCommentDto;
import com.example.defecttrackerserver.core.defect.defectComment.DefectCommentRepository;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImage;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImageDto;
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
import com.example.defecttrackerserver.core.lot.lot.LotDto;
import com.example.defecttrackerserver.core.lot.lot.LotRepository;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.UserDto;
import com.example.defecttrackerserver.core.user.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class DefectMapperTest {

    @InjectMocks
    private DefectMapper defectMapper;

    @Mock
    private DefectStatusRepository defectStatusRepository;

    @Mock
    private DefectCommentRepository  defectCommentRepository;

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

    DefectDto defectDto;
    UserDto userDto;
    DefectCommentDto defectCommentDto;
    DefectImageDto defectImageDto;
    ActionDto actionDto;
    LotDto lotDto;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        defectDto = new DefectDto();
        defectDto.setId(1);
        defectDto.setDefectStatus("testStatus");

        defectCommentDto = new DefectCommentDto();
        defectCommentDto.setId(1);
        defectDto.setDefectComments(Set.of(defectCommentDto));

        lotDto = new LotDto();
        lotDto.setId(1);
        defectDto.setLot(lotDto);
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
        defectDto.setCreatedOn(LocalDateTime.of(2023,2,1,0,0));
    }

    @Test
    void shouldReturnMappedDefect() {
        DefectStatus  defectStatus = new DefectStatus();
        defectStatus.setName("testStatus");

        Lot lot = new Lot();
        lot.setId(1);

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
        when(lotRepository.findById(any(Integer.class))).thenReturn(Optional.of(lot));
        when(locationRepository.findByName(any(String.class))).thenReturn(Optional.of(location));
        when(processRepository.findByName(any(String.class))).thenReturn(Optional.of(process));
        when(defectTypeRepository.findByName(any(String.class))).thenReturn(Optional.of(defectType));
        when(defectImageRepository.findById(any(Integer.class))).thenReturn(Optional.of(new DefectImage()));
        when(actionRepository.findById(any(Integer.class))).thenReturn(Optional.of(new Action()));
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(user));

        Defect defect = new Defect();
        Defect mappedDefect = defectMapper.map(defectDto, defect);

        assertEquals(defectDto.getDefectStatus(), mappedDefect.getDefectStatus().getName());
        assertEquals(defectDto.getDefectComments().size(), mappedDefect.getDefectComments().size());
        assertEquals(defectDto.getLot().getId(), mappedDefect.getLot().getId());
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
        when(lotRepository.findById(any(Integer.class))).thenReturn(Optional.of(new Lot()));
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
        when(lotRepository.findById(any(Integer.class))).thenReturn(Optional.of(new Lot()));
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
        when(lotRepository.findById(any(Integer.class))).thenReturn(Optional.of(new Lot()));
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
    void shouldThrowExceptionWhenNullOrEmptyFields() {
        defectDto.setDefectStatus(null);
        assertThrows(IllegalArgumentException.class, () -> defectMapper.checkNullOrEmptyFields(defectDto));

        defectDto.setDefectStatus("test");
        defectDto.setLot(null);
        assertThrows(IllegalArgumentException.class, () -> defectMapper.checkNullOrEmptyFields(defectDto));

        defectDto.setLot(new LotDto());
        defectDto.setLocation(null);
        assertThrows(IllegalArgumentException.class, () -> defectMapper.checkNullOrEmptyFields(defectDto));

        defectDto.setLocation("test");
        defectDto.setProcess(null);
        assertThrows(IllegalArgumentException.class, () -> defectMapper.checkNullOrEmptyFields(defectDto));

        defectDto.setProcess("test");
        defectDto.setDefectType(null);
        assertThrows(IllegalArgumentException.class, () -> defectMapper.checkNullOrEmptyFields(defectDto));

        defectDto.setDefectType("test");
        defectDto.setCreatedBy(null);
        assertThrows(IllegalArgumentException.class, () -> defectMapper.checkNullOrEmptyFields(defectDto));
    }
}

