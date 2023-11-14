package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.TestHelper;
import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.action.ActionDto;
import com.example.defecttrackerserver.core.action.ActionMapper;
import com.example.defecttrackerserver.core.coreService.EntityService;
import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategory;
import com.example.defecttrackerserver.core.defect.defectComment.DefectComment;
import com.example.defecttrackerserver.core.defect.defectComment.DefectCommentDto;
import com.example.defecttrackerserver.core.defect.defectComment.DefectCommentMapper;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImage;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImageDto;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImageMapper;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatus;
import com.example.defecttrackerserver.core.defect.defectType.DefectType;
import com.example.defecttrackerserver.core.defect.process.Process;
import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.lot.lot.Lot;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.UserMapper;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class DefectMapperTest {

    @Mock
    private EntityService entityService;

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

        when(entityService.getDefectStatusByName(any(String.class))).thenReturn(defectStatus);
        when(entityService.getCausationCategoryByName(any(String.class))).thenReturn(causationCategory);
        when(entityService.getDefectCommentById(any(Integer.class))).thenReturn(new DefectComment());
        when(entityService.getLotById(any(Integer.class))).thenReturn(lot);
        when(entityService.getLocationByName(any(String.class))).thenReturn(location);
        when(entityService.getProcessByName(any(String.class))).thenReturn(process);
        when(entityService.getDefectTypeByName(any(String.class))).thenReturn(defectType);
        when(entityService.getDefectImageById(any(Integer.class))).thenReturn(new DefectImage());
        when(entityService.getActionById(any(Integer.class))).thenReturn(new Action());
        when(entityService.getUserById(any(Integer.class))).thenReturn(user);

        Defect mappedDefect = defectMapper.map(defectDto, new Defect());

        assertEquals(defectDto.getDefectStatus(), mappedDefect.getDefectStatus().getName());
        assertEquals(defectDto.getCausationCategory(), mappedDefect.getCausationCategory().getName());
        assertEquals(defectDto.getDescription(), mappedDefect.getDescription());
        assertEquals(defectDto.getDefectComments().size(), mappedDefect.getDefectComments().size());
        assertEquals(defectDto.getLot(), mappedDefect.getLot().getId());
        assertEquals(defectDto.getLocation(), mappedDefect.getLocation().getName());
        assertEquals(defectDto.getProcess(), mappedDefect.getProcess().getName());
        assertEquals(defectDto.getDefectType(), mappedDefect.getDefectType().getName());
        assertEquals(defectDto.getImages().size(), mappedDefect.getImages().size());
        assertEquals(defectDto.getActions().size(), mappedDefect.getActions().size());
        assertEquals(defectDto.getCreatedBy().getId(), mappedDefect.getCreatedBy().getId());
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
        when(entityService.getUserById(any())).thenReturn(user);
        when(userMapper.mapToDto(any())).thenReturn(userDto);

        DefectDto mappedDefectDto = defectMapper.mapToDto(defect);
        assertEquals(defect.getId(), mappedDefectDto.getId());
        assertEquals(defect.getDescription(), mappedDefectDto.getDescription());
        assertEquals(defect.getCreatedAt(), mappedDefectDto.getCreatedAt());
        assertEquals(defect.getChangedAt(), mappedDefectDto.getChangedAt());
        assertEquals(defect.getDefectStatus().getName(), mappedDefectDto.getDefectStatus());
        assertEquals(defect.getDefectComments().size(), mappedDefectDto.getDefectComments().size());
        assertEquals(defect.getLot().getId(), mappedDefectDto.getLot());
        assertEquals(defect.getLocation().getName(), mappedDefectDto.getLocation());
        assertEquals(defect.getProcess().getName(), mappedDefectDto.getProcess());
        assertEquals(defect.getDefectType().getName(), mappedDefectDto.getDefectType());
        assertEquals(defect.getImages().size(), mappedDefectDto.getImages().size());
        assertEquals(defect.getCreatedBy().getId(), mappedDefectDto.getCreatedBy().getId());
        assertEquals(defect.getChangedBy().getId(), mappedDefectDto.getChangedBy().getId());
    }
}

