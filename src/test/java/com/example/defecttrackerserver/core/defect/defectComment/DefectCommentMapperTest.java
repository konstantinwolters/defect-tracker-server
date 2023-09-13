package com.example.defecttrackerserver.core.defect.defectComment;

import com.example.defecttrackerserver.TestHelper;
import com.example.defecttrackerserver.core.user.user.User;
import com.example.defecttrackerserver.core.user.user.UserMapper;
import com.example.defecttrackerserver.core.user.user.UserRepository;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class DefectCommentMapperTest {

    @InjectMocks
    private DefectCommentMapper defectCommentMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    DefectComment defectComment;
    DefectCommentDto defectCommentDto;
    UserDto userDto;
    User user;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        user = TestHelper.setUpUser();
        userDto = TestHelper.setUpUserDto();
        defectComment = TestHelper.setUpDefectComment();
        defectCommentDto = TestHelper.setUpDefectCommentDto();
        defectCommentDto.setCreatedBy(userDto);
    }

    @Test
    void shouldReturnMappedDefectComment() {

        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(user));

        DefectComment mappedDefectComment = defectCommentMapper.map(defectCommentDto, new DefectComment());

        assertEquals(defectCommentDto.getContent(), mappedDefectComment.getContent());
        assertEquals(defectCommentDto.getCreatedAt(), mappedDefectComment.getCreatedAt());
        assertEquals(defectCommentDto.getCreatedBy().getId(), (mappedDefectComment.getCreatedBy().getId()));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> defectCommentMapper.map(defectCommentDto, new DefectComment()));
    }

    @Test
    void shouldReturnMappedDefectCommentDto() {
        when(userMapper.mapToDto(any(User.class))).thenReturn(userDto);

        DefectCommentDto mappedDefectCommentDto = defectCommentMapper.mapToDto(defectComment);

        assertEquals(defectComment.getId(), mappedDefectCommentDto.getId());
        assertEquals(defectComment.getContent(), mappedDefectCommentDto.getContent());
        assertEquals(defectComment.getCreatedAt(), mappedDefectCommentDto.getCreatedAt());
    }
}

