package com.example.defecttrackerserver.core.defect.defectImage;

import com.example.defecttrackerserver.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefectImageMapperTest {

    @InjectMocks
    private DefectImageMapper defectImageMapper;

    private DefectImage defectImage;
    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        defectImage  = TestHelper.setUpDefectImage();
    }

    @Test
    void shouldReturnMappedDefectImageDto() {

        DefectImageDto mappedDefectImage = defectImageMapper.mapToDto(defectImage);

        assertEquals(defectImage.getId(), mappedDefectImage.getId());
        assertEquals(defectImage.getPath(), mappedDefectImage.getPath());
    }
}

