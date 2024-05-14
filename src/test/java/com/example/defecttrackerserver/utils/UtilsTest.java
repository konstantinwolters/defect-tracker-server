package com.example.defecttrackerserver.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UtilsTest {

    @Mock
    private MultipartFile mockFile;

    @Mock
    private DefaultFileSystemOperations fileSystemOperations;

    @InjectMocks
    private Utils utils;

    @BeforeEach
    public void setUp() {
        utils.MAX_FILE_SIZE = 3 * 1024 * 1024;
    }

    @Test
    public void shouldConvertDateToTime() {
        LocalDateTime result = utils.convertToDateTime("2023-09-09");
        assertEquals(
                LocalDateTime.of(2023, 9, 9, 0, 0)
                , result
        );
    }

    @Test
    public void shouldConvertStringToListOfInteger() {
        List<Integer> result = utils.convertStringToListOfInteger("1,2,3");
        assertEquals(List.of(1, 2, 3), result);
    }

    @Test
    public void shouldValidateImageValid() {
        when(mockFile.getContentType()).thenReturn("image/jpeg");
        when(mockFile.getSize()).thenReturn(2 * 1024 * 1024L);

        assertDoesNotThrow(() -> utils.validateImage(mockFile));
    }

    @Test
    public void shouldThrowExceptionWhenInvalidFileType() {
        when(mockFile.getContentType()).thenReturn("image/png");

        assertThrows(IllegalArgumentException.class, () -> utils.validateImage(mockFile));
    }

    @Test
    public void shouldThrowExceptionWhenFileTooLarge() {
        when(mockFile.getContentType()).thenReturn("image/jpeg");
        when(mockFile.getSize()).thenReturn(4 * 1024 * 1024L); // 4 MB

        assertThrows(IllegalArgumentException.class, () -> utils.validateImage(mockFile));
    }

    @Test
    public void shouldMapToSetWithLocalDateTime() {
        List<LocalDateTime> list = Arrays.asList(
                LocalDateTime.of(2023, 9, 14, 12, 0),
                LocalDateTime.of(2023, 9, 15, 12, 0),
                LocalDateTime.of(2023, 9, 16, 12, 0)
        );

        Set<LocalDate> result = utils.mapToSet(list, LocalDateTime::toLocalDate);

        assertEquals(new HashSet<>(Arrays.asList(
                LocalDate.of(2023, 9, 14),
                LocalDate.of(2023, 9, 15),
                LocalDate.of(2023, 9, 16)
        )), result);
    }
}