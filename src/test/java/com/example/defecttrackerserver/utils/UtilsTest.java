package com.example.defecttrackerserver.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UtilsTest {

    @Mock
    private MultipartFile mockFile;

    @InjectMocks
    private Utils utils;

    @BeforeEach
    public void setUp(){
        utils.MAX_FILE_SIZE = 3 * 1024 * 1024;
    }

    @Test
    public void shouldConvertDateToTime() {
        LocalDateTime result = utils.convertToDateTime("2023-09-09");
        assertEquals(
                LocalDateTime.of(2023, 9 , 9 , 0 , 0)
                ,result
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
}