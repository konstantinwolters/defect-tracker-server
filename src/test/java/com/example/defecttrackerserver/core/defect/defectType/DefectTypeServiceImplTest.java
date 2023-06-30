package com.example.defecttrackerserver.core.defect.defectType;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefectTypeServiceImplTest {

    @Mock
    private DefectTypeRepository defectTypeRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DefectTypeServiceImpl defectTypeService;

    private DefectTypeDto defectTypeDto;
    private DefectType defectType;

    @BeforeEach
    void setUp() {
        defectTypeDto = new DefectTypeDto();
        defectTypeDto.setId(1);
        defectTypeDto.setName("testName");

        defectType = new DefectType();
        defectType.setId(1);
        defectType.setName("testName");
    }

    @Test
    void shouldSaveDefectType() {
        when(defectTypeRepository.save(any(DefectType.class))).thenReturn(defectType);
        when(modelMapper.map(any(DefectType.class), eq(DefectTypeDto.class))).thenReturn(defectTypeDto);

        DefectTypeDto result = defectTypeService.saveDefectType(defectTypeDto);

        assertNotNull(result);
        assertEquals(defectType.getName(), result.getName());
        verify(defectTypeRepository, times(1)).save(any(DefectType.class));
    }

    @Test
    void shouldReturnDefectTypeById() {
        when(defectTypeRepository.findById(any(Integer.class))).thenReturn(Optional.of(defectType));
        when(modelMapper.map(defectType, DefectTypeDto.class)).thenReturn(defectTypeDto);

        DefectTypeDto result = defectTypeService.getDefectTypeById(1);

        assertNotNull(result);
        assertEquals(defectType.getId(), result.getId());
        assertEquals(defectType.getName(), result.getName());
    }

    @Test
    void shouldReturnAllDefectType(){
        when(defectTypeRepository.findAll()).thenReturn(Arrays.asList(defectType));
        when(modelMapper.map(defectType, DefectTypeDto.class)).thenReturn(defectTypeDto);

        List<DefectTypeDto> result = defectTypeService.getAllDefectTypes();

        assertNotNull(result);
        assertEquals(defectType.getId(), result.get(0).getId());
        assertEquals(defectType.getName(), result.get(0).getName());
    }

    @Test
    void shouldUpdateDefectType() {
        when(defectTypeRepository.findById(any(Integer.class))).thenReturn(Optional.of(defectType));
        when(defectTypeRepository.save(any(DefectType.class))).thenReturn(defectType);
        when(modelMapper.map(any(DefectType.class), eq(DefectTypeDto.class))).thenReturn(defectTypeDto);

        DefectTypeDto result = defectTypeService.updateDefectType(defectTypeDto);

        assertNotNull(result);
        assertEquals(defectType.getId(), result.getId());
        assertEquals(defectType.getName(), result.getName());
        verify(defectTypeRepository, times(1)).save(defectType);
    }

    @Test
    void shouldDeleteDefectType() {
        when(defectTypeRepository.findById(any(Integer.class))).thenReturn(Optional.of(defectType));

        defectTypeService.deleteDefectType(1);

        verify(defectTypeRepository, times(1)).delete(defectType);
    }

    @Test
    void shouldThrowExceptionWhenDefectTypeNotFound(){
        when(defectTypeRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> defectTypeService.deleteDefectType(1));
    }
}
