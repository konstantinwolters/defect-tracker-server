package com.example.defecttrackerserver.core.defect.defectImage;

import com.example.defecttrackerserver.TestHelper;
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
import org.springframework.mock.web.MockMultipartFile;

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
        defectImageDto = TestHelper.setUpDefectImageDto();
        defectImage = TestHelper.setUpDefectImage();
        defect = TestHelper.setUpDefect();
    }

    @Test
    void shouldAddDefectImageToDefect() {
        // Create a mock MultipartFile
        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpg", "some-image-data".getBytes());

        when(defectRepository.findById(any(Integer.class))).thenReturn(Optional.of(defect));
        when(defectImageMapper.mapToDto(any(DefectImage.class))).thenReturn(defectImageDto);

        DefectImageDto result = defectImageService.saveDefectImageToDefect(1, image);

        assertNotNull(result);
        assertEquals(defectImage.getUuid(), result.getUuid());
        assertEquals(2, defect.getImages().size());
    }

    @Test
    void shouldReturnDefectImageById() {
        when(defectImageRepository.findById(1)).thenReturn(Optional.ofNullable(defectImage));
        when(defectImageMapper.mapToDto(defectImage)).thenReturn(defectImageDto);

        DefectImageDto result = defectImageService.getDefectImageById(1);

        assertNotNull(result);
        assertEquals(defectImage.getId(), result.getId());
        assertEquals(defectImage.getUuid(), result.getUuid());
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
