package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.TestHelper;
import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategory;
import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategoryMapper;
import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategoryRepository;
import com.example.defecttrackerserver.core.defect.defectImage.DefectImage;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatus;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusMapper;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusRepository;
import com.example.defecttrackerserver.core.defect.defectType.DefectType;
import com.example.defecttrackerserver.core.defect.defectType.DefectTypeMapper;
import com.example.defecttrackerserver.core.defect.defectType.DefectTypeRepository;
import com.example.defecttrackerserver.core.defect.process.ProcessMapper;
import com.example.defecttrackerserver.core.location.LocationMapper;
import com.example.defecttrackerserver.core.lot.material.MaterialMapper;
import com.example.defecttrackerserver.core.lot.supplier.SupplierMapper;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.UserMapper;
import com.example.defecttrackerserver.response.PaginatedResponse;
import com.example.defecttrackerserver.security.SecurityService;
import com.example.defecttrackerserver.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefectServiceImplTest {

    @Mock
    private Utils utils;

    @Mock
    private DefectRepository defectRepository;

    @Mock DefectSpecification defectSpecification;

    @Mock
    private DefectStatusRepository defectStatusRepository;

    @Mock
    private CausationCategoryRepository causationCategoryRepository;

    @Mock
    private DefectTypeRepository defectTypeRepository;

    @Mock
    private DefectMapper defectMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private MaterialMapper materialMapper;

    @Mock
    private SupplierMapper supplierMapper;

    @Mock
    private LocationMapper locationMapper;

    @Mock
    private ProcessMapper processMapper;

    @Mock
    private DefectStatusMapper defectStatusMapper;

    @Mock
    private CausationCategoryMapper causationCategoryMapper;

    @Mock
    private DefectTypeMapper defectTypeMapper;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private DefectServiceImpl defectService;

    private DefectDto defectDto;
    private Defect defect;

    @BeforeEach
    void setUp() {
        defectDto = TestHelper.setUpDefectDto();
        defect = TestHelper.setUpDefect();
    }

    @Test
    void shouldSaveDefect() {
        User user = TestHelper.setUpUser();

        when(defectRepository.save(defect)).thenReturn(defect);
        when(defectStatusRepository.findByName(anyString())).thenReturn(Optional.of(new DefectStatus()));
        when(causationCategoryRepository.findByName(anyString())).thenReturn(Optional.of(new CausationCategory()));
        when(defectTypeRepository.findByName(anyString())).thenReturn(Optional.of(new DefectType()));
        when(defectMapper.map(any(DefectDto.class), any(Defect.class))).thenReturn(defect);
        when(defectMapper.mapToDto(defect)).thenReturn(defectDto);
        when(securityService.getUser()).thenReturn(user);
        when(utils.uploadImage(any(MultipartFile.class))).thenReturn("randomString");

        MockMultipartFile[] mockFiles = {
                new MockMultipartFile("image1", "image1.jpg", "image/jpg", "some-image-data".getBytes()),
                new MockMultipartFile("image2", "image2.jpg", "image/jpg", "some-other-image-data".getBytes())
        };

        DefectDto result = defectService.saveDefect(defectDto, mockFiles);

        assertNotNull(result);
        verify(defectRepository, times(2)).save(defect);
    }


    @Test
    void shouldReturnDefectById() {
        when(defectRepository.findById(1)).thenReturn(Optional.ofNullable(defect));
        when(defectMapper.mapToDto(defect)).thenReturn(defectDto);

        DefectDto result = defectService.getDefectById(1);

        assertNotNull(result);
        assertEquals(defect.getId(), result.getId());
        assertEquals(defect.getId(), result.getId());
    }

    @Test
    public void shouldReturnFilteredDefects() {
        String searchTerm = "Test";
        String lotIds = "1,2";
        String materialIds = "1,2";
        String supplierIds = "1,2";
        String defectStatusIds = "1,2";
        String causationCategoryIds = "1,2";
        LocalDate createdAtStart = LocalDate.now();
        LocalDate createdAtEnd = LocalDate.now();
        LocalDate changedAtStart = LocalDate.now();
        LocalDate changedAtEnd = LocalDate.now();
        String locationIds = "1,2";
        String processIds = "1,2";
        String defectTypeIds = "1,2";
        String createdByIds = "1,2";
        String changedByIds = "1,2";
        Integer page = 0;
        Integer size = 10;
        String sort = "id,desc";
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        Page<Defect> pageObject = new PageImpl<>(Arrays.asList(defect));

        Specification<Defect> spec = mock(Specification.class);

        when(defectSpecification.createSpecification(
                eq(searchTerm), anyList(), anyList(), anyList(), anyList(), anyList(), any(), any(),any(),
                any(), anyList(), anyList(), anyList(), anyList(), anyList()
        )).thenReturn(spec);
        when(defectRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(pageObject);
        when(defectMapper.mapToDto(defect)).thenReturn(defectDto);

        PaginatedResponse<DefectDto> result = defectService.getDefects(searchTerm, lotIds, materialIds, supplierIds,
                defectStatusIds, causationCategoryIds ,createdAtStart, createdAtEnd, changedAtStart, changedAtEnd,
                locationIds, processIds, defectTypeIds, createdByIds, changedByIds, page, size, sort);

        assertEquals(1, result.getContent().size());
        assertTrue(result.getContent().contains(defectDto));
        assertEquals(pageObject.getTotalPages(), result.getTotalPages());
        assertEquals((int) pageObject.getTotalElements(),result.getTotalElements());
        assertEquals(pageObject.getNumber(), result.getCurrentPage());
    }

    @Test
    void shouldUpdateDefect() {
        // Setup test data
        DefectImage testImage = new DefectImage();
        testImage.setId(100);
        List<DefectImage> imageList = new ArrayList<>();
        imageList.add(testImage);

        defect.setImages(imageList);
        defectDto.setImages(new ArrayList<>());

        // Mocking
        when(defectRepository.findById(any(Integer.class))).thenReturn(Optional.of(defect));
        when(defectRepository.save(any(Defect.class))).thenReturn(defect);
        when(defectMapper.map(any(DefectDto.class), any(Defect.class))).thenReturn(defect);
        when(defectMapper.mapToDto(any(Defect.class))).thenReturn(defectDto);
        when(securityService.getUser()).thenReturn(new User());

        // Test execution
        DefectDto result = defectService.updateDefect(1, defectDto, null);

        // Verifications
        assertNotNull(result);
        assertEquals(defect.getId(), result.getId());
        verify(defectRepository, times(1)).save(defect);
        verify(utils, times(1)).removeImage(testImage.getUuid());
    }

    @Test
    void shouldDeleteDefect() {
        when(defectRepository.findById(any(Integer.class))).thenReturn(Optional.of(defect));

        defectService.deleteDefect(1);

        verify(defectRepository, times(1)).delete(defect);
    }
}
