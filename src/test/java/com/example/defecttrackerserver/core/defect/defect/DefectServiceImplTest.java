package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatus;
import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusRepository;
import com.example.defecttrackerserver.core.lot.lot.Lot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefectServiceImplTest {

    @Mock
    private DefectRepository defectRepository;

    @Mock
    private DefectStatusRepository defectStatusRepository;

    @Mock
    private DefectMapper defectMapper;

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
    void shouldReturnAllDefects() {
        when(defectRepository.findAll()).thenReturn(Arrays.asList(defect));
        when(defectMapper.mapToDto(defect)).thenReturn(defectDto);

        List<DefectDto> result = defectService.getAllDefects();

        assertNotNull(result);
        assertEquals(defect.getId(), result.get(0).getId());
    }

    @Test
    public void shouldReturnFilteredDefects() {
        List<Integer> lotIds = Arrays.asList(1, 2);
        List<Integer> defectStatusIds = Arrays.asList(1, 2);
        String startDate = "2023-01-01";
        String endDate = "2023-01-31";
        List<Integer> locationIds = Arrays.asList(1, 2);
        List<Integer> processIds = Arrays.asList(1, 2);
        List<Integer> defectTypeIds = Arrays.asList(1, 2);
        List<Integer> createdByIds = Arrays.asList(1, 2);

        when(defectRepository.findAll(any(Specification.class))).thenReturn(Arrays.asList(defect));
        when(defectMapper.mapToDto(defect)).thenReturn(defectDto);

        List<DefectDto> result = defectService.getFilteredDefects(lotIds, defectStatusIds, startDate, endDate, locationIds, processIds, defectTypeIds, createdByIds);

        assertEquals(1, result.size());
        assertEquals(defectDto, result.get(0));
    }

    @Test
    void shouldUpdateDefect() {
        when(defectRepository.save(any(Defect.class))).thenReturn(defect);
        when(defectRepository.findById(any(Integer.class))).thenReturn(Optional.of(defect));
        when(defectStatusRepository.findByName(any(String.class))).thenReturn(Optional.of(new DefectStatus()));
        when(defectMapper.map(any(DefectDto.class), any(Defect.class))).thenReturn(defect);
        when(defectMapper.mapToDto(any(Defect.class))).thenReturn(defectDto);

        DefectDto result = defectService.updateDefect(defectDto);

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
