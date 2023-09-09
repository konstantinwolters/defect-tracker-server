package com.example.defecttrackerserver.core.defect.defectComment;

import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import com.example.defecttrackerserver.security.SecurityService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefectCommentServiceImplTest {
    @Mock
    private DefectCommentRepository defectCommentRepository;

    @Mock
    private DefectRepository defectRepository;

    @Mock
    private SecurityService securityService;

    @Mock
    private DefectCommentMapper defectCommentMapper;

    @InjectMocks
    private DefectCommentServiceImpl defectCommentService;

    private DefectCommentDto  defectCommentDto;
    private DefectComment defectComment;

    private Defect defect;

    @BeforeEach
    void setUp() {
        defectCommentDto = new DefectCommentDto();
        defectCommentDto.setId(1);
        defectCommentDto.setContent("testContent");
        defectCommentDto.setCreatedBy(new UserDto());

        User user = new User();
        user.setUsername("testUser");

        defectComment = new DefectComment();
        defectComment.setId(1);
        defectComment.setContent("testContent");
        defectComment.setCreatedBy(user);

        defect = new Defect();
        defect.setId(1);
    }

    @Test
    void shouldAddDefectCommentToDefect() {
        when(defectRepository.findById(any(Integer.class))).thenReturn(Optional.of(defect));
        when(defectCommentMapper.map(any(DefectCommentDto.class), any(DefectComment.class))).thenReturn(defectComment);
        when(defectCommentMapper.mapToDto(defectComment)).thenReturn(defectCommentDto);

        DefectCommentDto result = defectCommentService.addDefectCommentToDefect(1, defectCommentDto);

        assertNotNull(result);
        assertEquals(defectComment.getContent(), result.getContent());
        assertTrue(defect.getDefectComments().contains(defectComment));
    }

    @Test
    void shouldReturnDefectCommentById() {
        when(defectCommentRepository.findById(1)).thenReturn(Optional.ofNullable(defectComment));
        when(defectCommentMapper.mapToDto(defectComment)).thenReturn(defectCommentDto);

        DefectCommentDto result = defectCommentService.getDefectCommentById(1);

        assertNotNull(result);
        assertEquals(defectComment.getId(), result.getId());
        assertEquals(defectComment.getContent(), result.getContent());
    }

    @Test
    void shouldUpdateDefectComment() {
        when(defectCommentRepository.findById(any(Integer.class))).thenReturn(Optional.of(defectComment));
        when(defectCommentRepository.save(any(DefectComment.class))).thenReturn(defectComment);
        when(defectCommentMapper.map(any(DefectCommentDto.class), any(DefectComment.class))).thenReturn(defectComment);
        when(defectCommentMapper.mapToDto(any(DefectComment.class))).thenReturn(defectCommentDto);

        DefectCommentDto result = defectCommentService.updateDefectComment(1, defectCommentDto);

        assertNotNull(result);
        assertEquals(defectComment.getId(), result.getId());
        assertEquals(defectComment.getContent(), result.getContent());
        verify(defectCommentRepository, times(1)).save(defectComment);
    }

    @Test
    void shouldDeleteDefectComment() {
        Defect defectSpy = spy(new Defect());
        when(defectRepository.findById(any(Integer.class))).thenReturn(Optional.of(defectSpy));
        when(defectCommentRepository.findById(any(Integer.class))).thenReturn(Optional.of(defectComment));

        defectCommentService.deleteDefectComment(1,1);

        verify(defectSpy, times(1)).deleteDefectComment(defectComment);
        assertFalse(defectSpy.getDefectComments().contains(defectComment));
    }

    @Test
    void shouldThrowExceptionWhenDefectNotFound(){
        when(defectRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> defectCommentService.deleteDefectComment(1,1));
    }

    @Test
    void shouldThrowExceptionWhenDefectCommentNotFound(){
        when(defectRepository.findById(any(Integer.class))).thenReturn(Optional.of(defect));
        when(defectCommentRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> defectCommentService.deleteDefectComment(1,1));
    }
}
