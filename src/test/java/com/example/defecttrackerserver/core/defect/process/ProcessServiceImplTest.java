package com.example.defecttrackerserver.core.defect.process;

import com.example.defecttrackerserver.core.defect.defectType.DefectType;
import com.example.defecttrackerserver.core.defect.defectType.DefectTypeDto;
import com.example.defecttrackerserver.core.defect.defectType.DefectTypeRepository;
import com.example.defecttrackerserver.core.defect.defectType.DefectTypeServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProcessServiceImplTest {

    @Mock
    private ProcessRepository processRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProcessServiceImpl processService;

    private ProcessDto processDto;
    private Process process;

    @BeforeEach
    void setUp() {
        processDto = new ProcessDto();
        processDto.setId(1);
        processDto.setName("testName");

        process = new Process();
        process.setId(1);
        process.setName("testName");
    }

    @Test
    void shouldSaveProcess() {
        when(processRepository.save(any(Process.class))).thenReturn(process);
        when(modelMapper.map(any(Process.class), eq(ProcessDto.class))).thenReturn(processDto);

        ProcessDto result = processService.saveProcess(processDto);

        assertNotNull(result);
        assertEquals(process.getName(), result.getName());
        verify(processRepository, times(1)).save(any(Process.class));
    }

    @Test
    void shouldReturnProcessById() {
        when(processRepository.findById(any(Integer.class))).thenReturn(Optional.of(process));
        when(modelMapper.map(process, ProcessDto.class)).thenReturn(processDto);

        ProcessDto result = processService.getProcessById(1);

        assertNotNull(result);
        assertEquals(process.getId(), result.getId());
        assertEquals(process.getName(), result.getName());
    }

    @Test
    void shouldReturnAllProcesses(){
        when(processRepository.findAll()).thenReturn(Arrays.asList(process));
        when(modelMapper.map(process, ProcessDto.class)).thenReturn(processDto);

        List<ProcessDto> result = processService.getAllProcesses();

        assertNotNull(result);
        assertEquals(process.getId(), result.get(0).getId());
        assertEquals(process.getName(), result.get(0).getName());
    }

    @Test
    void shouldUpdateProcess() {
        when(processRepository.findById(any(Integer.class))).thenReturn(Optional.of(process));
        when(processRepository.save(any(Process.class))).thenReturn(process);
        when(modelMapper.map(any(Process.class), eq(ProcessDto.class))).thenReturn(processDto);

        ProcessDto result = processService.updateProcess(processDto);

        assertNotNull(result);
        assertEquals(process.getId(), result.getId());
        assertEquals(process.getName(), result.getName());
        verify(processRepository, times(1)).save(process);
    }

    @Test
    void shouldDeleteProcess() {
        when(processRepository.findById(any(Integer.class))).thenReturn(Optional.of(process));

        processService.deleteProcess(1);

        verify(processRepository, times(1)).delete(process);
    }

    @Test
    void shouldThrowExceptionWhenProcessNotFound(){
        when(processRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> processService.deleteProcess(1));
    }
}
