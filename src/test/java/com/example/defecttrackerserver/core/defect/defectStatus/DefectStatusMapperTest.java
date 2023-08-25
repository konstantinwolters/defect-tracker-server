package com.example.defecttrackerserver.core.defect.defectStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefectStatusMapperTest {

    @InjectMocks
    private DefectStatusMapper defectStatusMapper;

    private DefectStatus defectStatus;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        defectStatus  = new DefectStatus();
        defectStatus.setId(1);
        defectStatus.setName("Test");
    }

    @Test
    void shouldReturnMappedDefectStatusDto() {

        DefectStatusDto mappedDefectStatus = defectStatusMapper.mapToDto(defectStatus);

        assertEquals(defectStatus.getId(), mappedDefectStatus.getId());
        assertEquals(defectStatus.getName(), mappedDefectStatus.getName());
    }
}

