package com.example.defecttrackerserver.core.defect.causationCategory;

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
public class CausationCategoryServiceImplTest {

    @Mock
    private DefectRepository defectRepository;

    @Mock
    private CausationCategoryRepository causationCategoryRepository;

    @Mock
    private CausationCategoryMapper causationCategoryMapper;

    @InjectMocks
    private CausationCategoryServiceImpl causationCategoryService;

    private CausationCategoryDto causationCategoryDto;
    private CausationCategory causationCategory;

    @BeforeEach
    void setUp() {
        causationCategoryDto = TestHelper.setUpCausationCategoryDto();

        causationCategory = TestHelper.setUpCausationCategory();
    }

    @Test
    void shouldSaveDefectStatus() {
        when(causationCategoryRepository.save(any(CausationCategory.class))).thenReturn(causationCategory);
        when(causationCategoryMapper.mapToDto(any(CausationCategory.class))).thenReturn(causationCategoryDto);

        CausationCategoryDto result = causationCategoryService.saveCausationCategory(causationCategoryDto);

        assertNotNull(result);
        assertEquals(causationCategory.getName(), result.getName());
        verify(causationCategoryRepository, times(1)).save(any(CausationCategory.class));
    }

    @Test
    void shouldReturnDefectStatusById() {
        when(causationCategoryRepository.findById(any(Integer.class))).thenReturn(Optional.of(causationCategory));
        when(causationCategoryMapper.mapToDto(causationCategory)).thenReturn(causationCategoryDto);

        CausationCategoryDto result = causationCategoryService.getCausationCategoryById(1);

        assertNotNull(result);
        assertEquals(causationCategory.getId(), result.getId());
        assertEquals(causationCategory.getName(), result.getName());
    }

    @Test
    void shouldReturnAllDefectStatus(){
        when(causationCategoryRepository.findAll()).thenReturn(Arrays.asList(causationCategory));
        when(causationCategoryMapper.mapToDto(causationCategory)).thenReturn(causationCategoryDto);

        List<CausationCategoryDto> result = causationCategoryService.getAllCausationCategories();

        assertNotNull(result);
        assertEquals(causationCategory.getId(), result.get(0).getId());
        assertEquals(causationCategory.getName(), result.get(0).getName());
    }

    @Test
    void shouldUpdateDefectStatus() {
        when(causationCategoryRepository.findById(any(Integer.class))).thenReturn(Optional.of(causationCategory));
        when(causationCategoryRepository.save(any(CausationCategory.class))).thenReturn(causationCategory);
        when(causationCategoryMapper.mapToDto(any(CausationCategory.class))).thenReturn(causationCategoryDto);

        CausationCategoryDto result = causationCategoryService
                .updateCausationCategory(1, causationCategoryDto);

        assertNotNull(result);
        assertEquals(causationCategory.getId(), result.getId());
        assertEquals(causationCategory.getName(), result.getName());
        verify(causationCategoryRepository, times(1)).save(causationCategory);
    }

    @Test
    void shouldDeleteDefectStatus() {
        when(causationCategoryRepository.findById(any(Integer.class))).thenReturn(Optional.of(causationCategory));
        when(defectRepository.findByCausationCategoryId(any(Integer.class))).thenReturn(Set.of());

        causationCategoryService.deleteCausationCategory(1);

        verify(causationCategoryRepository, times(1)).delete(causationCategory);
    }

    @Test
    void shouldThrowExceptionWhenDefectStatusNotFound(){
        when(causationCategoryRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> causationCategoryService.deleteCausationCategory(1));
    }
}
