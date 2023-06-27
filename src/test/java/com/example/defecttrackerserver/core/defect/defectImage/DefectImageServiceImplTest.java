package com.example.defecttrackerserver.core.defect.defectImage;

import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import com.example.defecttrackerserver.core.defect.defectComment.*;
import com.example.defecttrackerserver.core.user.user.UserDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefectImageServiceImplTest {

    @Mock
    private DefectImageRepository defectImageRepository;

    @Mock
    private DefectRepository defectRepository;

    @Mock
    private ModelMapper modelMapper;

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
        when(modelMapper.map(any(DefectImage.class), eq(DefectImageDto.class))).thenReturn(defectImageDto);

        DefectImageDto result = defectImageService.saveDefectImageToDefect(1, defectImageDto);

        assertNotNull(result);
        assertEquals(defectImage.getPath(), result.getPath());
        verify(defectRepository, times(1)).save(defect);
        assertEquals(1, defect.getImages().size());
    }

    @Test
    void shouldReturnDefectImageById() {
        when(defectImageRepository.findById(1)).thenReturn(Optional.ofNullable(defectImage));
        when(modelMapper.map(defectImage, DefectImageDto.class)).thenReturn(defectImageDto);

        DefectImageDto result = defectImageService.getDefectImageById(1);

        assertNotNull(result);
        assertEquals(defectImage.getId(), result.getId());
        assertEquals(defectImage.getPath(), result.getPath());
    }

    @Test
    void shouldUpdateDefectImage() {
        when(defectImageRepository.findById(any(Integer.class))).thenReturn(Optional.of(defectImage));
        when(defectImageRepository.save(any(DefectImage.class))).thenReturn(defectImage);
        when(modelMapper.map(any(DefectImage.class), eq(DefectImageDto.class))).thenReturn(defectImageDto);

        DefectImageDto result = defectImageService.updateDefectImage(defectImageDto);

        assertNotNull(result);
        assertEquals(defectImage.getId(), result.getId());
        assertEquals(defectImage.getPath(), result.getPath());
        verify(defectImageRepository, times(1)).save(defectImage);
    }

    @Test
    void shouldDeleteDefectImage() {
        Defect defectSpy = spy(new Defect());
        when(defectRepository.findById(any(Integer.class))).thenReturn(Optional.of(defectSpy));
        when(defectImageRepository.findById(any(Integer.class))).thenReturn(Optional.of(defectImage));

        defectImageService.deleteDefectImageFromDefect(1,1);

        verify(defectSpy, times(1)).deleteDefectImage(defectImage);
        verify(defectRepository, times(1)).save(defectSpy);
        assertFalse(defectSpy.getImages().contains(defectImage));
    }

    @Test
    void shouldThrowExceptionWhenDefectNotFound(){
        when(defectRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> defectImageService.deleteDefectImageFromDefect(1,1));
    }

    @Test
    void shouldThrowExceptionWhenDefectCommentNotFound(){
        when(defectRepository.findById(any(Integer.class))).thenReturn(Optional.of(defect));
        when(defectImageRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> defectImageService.deleteDefectImageFromDefect(1,1));
    }
}
