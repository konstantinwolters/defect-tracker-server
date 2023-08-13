package com.example.defecttrackerserver.core.defect.defectStatus;

import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefectStatusServiceImplTest {

    @Mock
    private DefectStatusRepository defectStatusRepository;

    @Mock
    private DefectRepository defectRepository;

    @Mock
    private DefectStatusMapper defectStatusMapper;

    @InjectMocks
    private DefectStatusServiceImpl defectStatusService;

    private DefectStatusDto defectStatusDto;
    private DefectStatus defectStatus;

    @BeforeEach
    void setUp() {
        defectStatusDto = new DefectStatusDto();
        defectStatusDto.setId(1);
        defectStatusDto.setName("testName");

        defectStatus = new DefectStatus();
        defectStatus.setId(1);
        defectStatus.setName("testName");
    }

    @Test
    void shouldSaveDefectStatus() {
        when(defectStatusRepository.save(any(DefectStatus.class))).thenReturn(defectStatus);
        when(defectStatusMapper.mapToDto(any(DefectStatus.class))).thenReturn(defectStatusDto);

        DefectStatusDto result = defectStatusService.saveDefectStatus(defectStatusDto);

        assertNotNull(result);
        assertEquals(defectStatus.getName(), result.getName());
        verify(defectStatusRepository, times(1)).save(any(DefectStatus.class));
    }

    @Test
    void shouldReturnDefectStatusById() {
        when(defectStatusRepository.findById(any(Integer.class))).thenReturn(Optional.of(defectStatus));
        when(defectStatusMapper.mapToDto(defectStatus)).thenReturn(defectStatusDto);

        DefectStatusDto result = defectStatusService.getDefectStatusById(1);

        assertNotNull(result);
        assertEquals(defectStatus.getId(), result.getId());
        assertEquals(defectStatus.getName(), result.getName());
    }

    @Test
    void shouldReturnAllDefectStatus(){
        when(defectStatusRepository.findAll()).thenReturn(Arrays.asList(defectStatus));
        when(defectStatusMapper.mapToDto(defectStatus)).thenReturn(defectStatusDto);

        List<DefectStatusDto> result = defectStatusService.getAllDefectStatus();

        assertNotNull(result);
        assertEquals(defectStatus.getId(), result.get(0).getId());
        assertEquals(defectStatus.getName(), result.get(0).getName());
    }

    @Test
    void shouldUpdateDefectStatus() {
        when(defectStatusRepository.findById(any(Integer.class))).thenReturn(Optional.of(defectStatus));
        when(defectStatusRepository.save(any(DefectStatus.class))).thenReturn(defectStatus);
        when(defectStatusMapper.mapToDto(any(DefectStatus.class))).thenReturn(defectStatusDto);

        DefectStatusDto result = defectStatusService.updateDefectStatus(1, defectStatusDto);

        assertNotNull(result);
        assertEquals(defectStatus.getId(), result.getId());
        assertEquals(defectStatus.getName(), result.getName());
        verify(defectStatusRepository, times(1)).save(defectStatus);
    }

    @Test
    void shouldDeleteDefectStatus() {
        when(defectStatusRepository.findById(any(Integer.class))).thenReturn(Optional.of(defectStatus));
        when(defectRepository.findByDefectStatusId(any(Integer.class))).thenReturn(Set.of());

        defectStatusService.deleteDefectStatus(1);

        verify(defectStatusRepository, times(1)).delete(defectStatus);
    }

    @Test
    void shouldThrowExceptionWhenDefectStatusNotFound(){
        when(defectStatusRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> defectStatusService.deleteDefectStatus(1));
    }
}
