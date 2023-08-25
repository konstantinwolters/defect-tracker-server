package com.example.defecttrackerserver.core.defect.process;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProcessMapperTest {

    @InjectMocks
    private ProcessMapper processMapper;

    private Process process;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        process = new Process();
        process.setId(1);
        process.setName("Test");
    }

    @Test
    void shouldReturnMappedProcessDto() {

        ProcessDto mappedProcess = processMapper.mapToDto(process);

        assertEquals(process.getId(), mappedProcess.getId());
        assertEquals(process.getName(), mappedProcess.getName());
    }
}

