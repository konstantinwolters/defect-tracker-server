package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatus;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusRepository;
import com.example.defecttrackerserver.core.lot.lot.Lot;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.response.PaginatedResponse;
import com.example.defecttrackerserver.security.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefectServiceImplTest {

    @Mock
    private DefectRepository defectRepository;

    @Mock
    private DefectStatusRepository defectStatusRepository;

    @Mock
    private DefectMapper defectMapper;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private DefectServiceImpl defectService;

    private DefectDto defectDto;
    private Defect defect;

    @BeforeEach
    void setUp() {
        defectDto = new DefectDto();
        defectDto.setId(1);
        defectDto.setDefectStatus("testStatus");

        defect = new Defect();
        defect.setId(1);
        defect.setLot(new Lot());
    }

    @Test
    void shouldSaveDefect() {
        when(defectRepository.save(defect)).thenReturn(defect);
        when(defectStatusRepository.findByName(anyString())).thenReturn(Optional.of(new DefectStatus()));
        when(defectMapper.map(any(DefectDto.class), any(Defect.class))).thenReturn(defect);
        when(defectMapper.mapToDto(defect)).thenReturn(defectDto);

        DefectDto result = defectService.saveDefect(defectDto);

        assertNotNull(result);
        verify(defectRepository, times(1)).save(defect);
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
        List<Integer> lotIds = Arrays.asList(1, 2);
        List<Integer> defectStatusIds = Arrays.asList(1, 2);
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        List<Integer> locationIds = Arrays.asList(1, 2);
        List<Integer> processIds = Arrays.asList(1, 2);
        List<Integer> defectTypeIds = Arrays.asList(1, 2);
        List<Integer> createdByIds = Arrays.asList(1, 2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Defect> page = new PageImpl<>(Arrays.asList(defect));

        when(defectRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
        when(defectMapper.mapToDto(defect)).thenReturn(defectDto);

        PaginatedResponse<DefectDto> result = defectService.getDefects(lotIds, defectStatusIds, startDate,
                endDate, locationIds, processIds, defectTypeIds, createdByIds, pageable);

        assertEquals(1, result.getContent().size());
        assertTrue(result.getContent().contains(defectDto));
        assertEquals(page.getTotalPages(), result.getTotalPages());
        assertEquals((int) page.getTotalElements(),result.getTotalElements());
        assertEquals(page.getNumber(), result.getCurrentPage());
    }

    @Test
    void shouldUpdateDefect() {
        when(defectRepository.save(any(Defect.class))).thenReturn(defect);
        when(defectRepository.findById(any(Integer.class))).thenReturn(Optional.of(defect));
        when(defectStatusRepository.findByName(any(String.class))).thenReturn(Optional.of(new DefectStatus()));
        when(defectMapper.map(any(DefectDto.class), any(Defect.class))).thenReturn(defect);
        when(defectMapper.mapToDto(any(Defect.class))).thenReturn(defectDto);
        when(securityService.getUser()).thenReturn(new User());

        DefectDto result = defectService.updateDefect(1, defectDto);

        assertNotNull(result);
        assertEquals(defect.getId(), result.getId());
        verify(defectRepository, times(1)).save(defect);
    }

    @Test
    void shouldDeleteDefect() {
        when(defectRepository.findById(any(Integer.class))).thenReturn(Optional.of(defect));

        defectService.deleteDefect(1);

        verify(defectRepository, times(1)).delete(defect);
    }
}
