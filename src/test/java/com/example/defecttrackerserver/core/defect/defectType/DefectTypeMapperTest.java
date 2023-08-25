package com.example.defecttrackerserver.core.defect.defectType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefectTypeMapperTest {

    @InjectMocks
    private DefectTypeMapper defectTypeMapper;

    private DefectType defectType;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        defectType  = new DefectType();
        defectType.setId(1);
        defectType.setName("Test");
    }

    @Test
    void shouldReturnMappedDefectImageDto() {

        DefectTypeDto mappedDefectType = defectTypeMapper.mapToDto(defectType);

        assertEquals(defectType.getId(), mappedDefectType.getId());
        assertEquals(defectType.getName(), mappedDefectType.getName());
    }
}

