package com.example.defecttrackerserver.core.defect.defectImage;

import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import com.example.defecttrackerserver.utils.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefectImageServiceImplTest {

    @Mock
    private Utils utils;

    @Mock
    private DefectImageRepository defectImageRepository;

    @Mock
    private DefectRepository defectRepository;

    @Mock
    private DefectImageMapper defectImageMapper;

    @InjectMocks
    private DefectImageServiceImpl defectImageService;

    private DefectImageDto defectImageDto;
    private DefectImage defectImage;

    private Defect defect;

    @BeforeEach
    void setUp() {
        defectImageDto = new DefectImageDto();
        defectImageDto.setId(1);
        defectImageDto.setPath("testPath");

        defectImage = new DefectImage();
        defectImage.setId(1);
        defectImage.setPath("testPath");

        defect = new Defect();
        defect.setId(1);
    }

    @Test
    void shouldAddDefectImageToDefect() {
        when(defectRepository.findById(any(Integer.class))).thenReturn(Optional.of(defect));
        when(defectImageMapper.mapToDto(any(DefectImage.class))).thenReturn(defectImageDto);

        DefectImageDto result = defectImageService.saveDefectImageToDefect(1, defectImageDto);

        assertNotNull(result);
        assertEquals(defectImage.getPath(), result.getPath());
        assertEquals(1, defect.getImages().size());
    }

    @Test
    void shouldReturnDefectImageById() {
        when(defectImageRepository.findById(1)).thenReturn(Optional.ofNullable(defectImage));
        when(defectImageMapper.mapToDto(defectImage)).thenReturn(defectImageDto);

        DefectImageDto result = defectImageService.getDefectImageById(1);

        assertNotNull(result);
        assertEquals(defectImage.getId(), result.getId());
        assertEquals(defectImage.getPath(), result.getPath());
    }

    @Test
    void shouldDeleteDefectImage() {
        Defect defectSpy = spy(new Defect());
        when(defectRepository.findById(any(Integer.class))).thenReturn(Optional.of(defectSpy));
        when(defectImageRepository.findById(any(Integer.class))).thenReturn(Optional.of(defectImage));

        defectImageService.deleteDefectImage(1,1);

        verify(defectSpy, times(1)).deleteDefectImage(defectImage);
        assertFalse(defectSpy.getImages().contains(defectImage));
    }

    @Test
    void shouldThrowExceptionWhenDefectNotFound(){
        when(defectRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> defectImageService.deleteDefectImage(1,1));
    }

    @Test
    void shouldThrowExceptionWhenDefectCommentNotFound(){
        when(defectRepository.findById(any(Integer.class))).thenReturn(Optional.of(defect));
        when(defectImageRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> defectImageService.deleteDefectImage(1,1));
    }
}
