package com.example.defecttrackerserver.core.defect.defectComment;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.action.ActionDto;
import com.example.defecttrackerserver.core.action.ActionMapper;
import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.UserDto;
import com.example.defecttrackerserver.core.user.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class DefectCommentMapperTest {

    @InjectMocks
    private DefectCommentMapper defectCommentMapper;

    @Mock
    private UserRepository userRepository;

    DefectCommentDto defectCommentDto;
    UserDto userDto;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        defectCommentDto = new DefectCommentDto();
        defectCommentDto.setId(1);
        defectCommentDto.setContent("testContent");
        defectCommentDto.setCreatedOn(LocalDateTime.now());

        userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("testName");

        defectCommentDto.setCreatedBy(userDto);
    }

    @Test
    void shouldReturnMappedAction() {
        User user = new User();
        user.setId(1);

        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(user));

        DefectComment defectComment = new DefectComment();
        DefectComment mappedDefectComment = defectCommentMapper.map(defectCommentDto, defectComment);

        assertEquals(defectCommentDto.getContent(), mappedDefectComment.getContent());
        assertEquals(defectCommentDto.getCreatedOn(), mappedDefectComment.getCreatedOn());
        assertEquals(defectCommentDto.getCreatedBy().getId(), (mappedDefectComment.getCreatedBy().getId()));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> defectCommentMapper.map(defectCommentDto, new DefectComment()));
    }

    @Test
    void shouldThrowExceptionWhenNullOrEmptyFields() {
        defectCommentDto.setContent("");
        assertThrows(IllegalArgumentException.class, () -> defectCommentMapper.checkNullOrEmptyFields(defectCommentDto));

        defectCommentDto.setContent("testContent");
        defectCommentDto.setCreatedOn(null);
        assertThrows(IllegalArgumentException.class, () -> defectCommentMapper.checkNullOrEmptyFields(defectCommentDto));

        defectCommentDto.setCreatedOn(LocalDateTime.now());
        defectCommentDto.setCreatedBy(null);
        assertThrows(IllegalArgumentException.class, () -> defectCommentMapper.checkNullOrEmptyFields(defectCommentDto));
    }
}

