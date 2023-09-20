package com.example.defecttrackerserver.core.defect.defectType;

import com.example.defecttrackerserver.TestHelper;
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
public class DefectTypeServiceImplTest {

    @Mock
    private DefectRepository defectRepository;

    @Mock
    private DefectTypeRepository defectTypeRepository;

    @Mock
    private DefectTypeMapper defectTypeMapper;

    @InjectMocks
    private DefectTypeServiceImpl defectTypeService;

    private DefectTypeDto defectTypeDto;
    private DefectType defectType;

    @BeforeEach
    void setUp() {
        defectTypeDto = TestHelper.setUpDefectTypeDto();
        defectType = TestHelper.setUpDefectType();
    }

    @Test
    void shouldSaveDefectType() {
        when(defectTypeRepository.save(any(DefectType.class))).thenReturn(defectType);
        when(defectTypeMapper.mapToDto(any(DefectType.class))).thenReturn(defectTypeDto);

        DefectTypeDto result = defectTypeService.saveDefectType(defectTypeDto);

        assertNotNull(result);
        assertEquals(defectType.getName(), result.getName());
        verify(defectTypeRepository, times(1)).save(any(DefectType.class));
    }

    @Test
    void shouldReturnDefectTypeById() {
        when(defectTypeRepository.findById(any(Integer.class))).thenReturn(Optional.of(defectType));
        when(defectTypeMapper.mapToDto(defectType)).thenReturn(defectTypeDto);

        DefectTypeDto result = defectTypeService.getDefectTypeById(1);

        assertNotNull(result);
        assertEquals(defectType.getId(), result.getId());
        assertEquals(defectType.getName(), result.getName());
    }

    @Test
    void shouldReturnAllDefectType(){
        when(defectTypeRepository.findAll()).thenReturn(Arrays.asList(defectType));
        when(defectTypeMapper.mapToDto(defectType)).thenReturn(defectTypeDto);

        List<DefectTypeDto> result = defectTypeService.getAllDefectTypes();

        assertNotNull(result);
        assertEquals(defectType.getId(), result.get(0).getId());
        assertEquals(defectType.getName(), result.get(0).getName());
    }

    @Test
    void shouldUpdateDefectType() {
        when(defectTypeRepository.findById(any(Integer.class))).thenReturn(Optional.of(defectType));
        when(defectTypeRepository.save(any(DefectType.class))).thenReturn(defectType);
        when(defectTypeMapper.mapToDto(any(DefectType.class))).thenReturn(defectTypeDto);

        DefectTypeDto result = defectTypeService.updateDefectType(1, defectTypeDto);

        assertNotNull(result);
        assertEquals(defectType.getId(), result.getId());
        assertEquals(defectType.getName(), result.getName());
        verify(defectTypeRepository, times(1)).save(defectType);
    }

    @Test
    void shouldDeleteDefectType() {
        when(defectTypeRepository.findById(any(Integer.class))).thenReturn(Optional.of(defectType));
        when(defectRepository.findByDefectTypeId(any(Integer.class))).thenReturn(Set.of());

        defectTypeService.deleteDefectType(1);

        verify(defectTypeRepository, times(1)).delete(defectType);
    }

    @Test
    void shouldNotDeleteDefaultDefectType(){
        DefectType defectType = TestHelper.setUpDefectType();
        defectType.setName("Undefined");

        when(defectTypeRepository.findById(anyInt())).thenReturn(Optional.of(defectType));
        when(defectRepository.findByDefectTypeId(any(Integer.class))).thenReturn(Set.of());

        assertThrows(UnsupportedOperationException.class,
                ()->defectTypeService.deleteDefectType(1));
    }

    @Test
    void shouldThrowExceptionWhenDefectTypeNotFound(){
        when(defectTypeRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> defectTypeService.deleteDefectType(1));
    }
}
