package com.example.defecttrackerserver.core.defect.process;

import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProcessServiceImplTest {

    @Mock
    private DefectRepository defectRepository;

    @Mock
    private ProcessRepository processRepository;

    @Mock
    private ProcessMapper processMapper;

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
        when(processMapper.mapToDto(any(Process.class))).thenReturn(processDto);

        ProcessDto result = processService.saveProcess(processDto);

        assertNotNull(result);
        assertEquals(process.getName(), result.getName());
        verify(processRepository, times(1)).save(any(Process.class));
    }

    @Test
    void shouldReturnProcessById() {
        when(processRepository.findById(any(Integer.class))).thenReturn(Optional.of(process));
        when(processMapper.mapToDto(process)).thenReturn(processDto);

        ProcessDto result = processService.getProcessById(1);

        assertNotNull(result);
        assertEquals(process.getId(), result.getId());
        assertEquals(process.getName(), result.getName());
    }

    @Test
    void shouldReturnAllProcesses(){
        when(processRepository.findAll()).thenReturn(Arrays.asList(process));
        when(processMapper.mapToDto(process)).thenReturn(processDto);

        List<ProcessDto> result = processService.getAllProcesses();

        assertNotNull(result);
        assertEquals(process.getId(), result.get(0).getId());
        assertEquals(process.getName(), result.get(0).getName());
    }

    @Test
    void shouldUpdateProcess() {
        when(processRepository.findById(any(Integer.class))).thenReturn(Optional.of(process));
        when(processRepository.save(any(Process.class))).thenReturn(process);
        when(processMapper.mapToDto(any(Process.class))).thenReturn(processDto);

        ProcessDto result = processService.updateProcess(1, processDto);

        assertNotNull(result);
        assertEquals(process.getId(), result.getId());
        assertEquals(process.getName(), result.getName());
        verify(processRepository, times(1)).save(process);
    }

    @Test
    void shouldDeleteProcess() {
        when(processRepository.findById(any(Integer.class))).thenReturn(Optional.of(process));
        when(defectRepository.findByProcessId(any(Integer.class))).thenReturn(Set.of());

        processService.deleteProcess(1);

        verify(processRepository, times(1)).delete(process);
    }

    @Test
    void shouldThrowExceptionWhenProcessNotFound(){
        when(processRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> processService.deleteProcess(1));
    }
}
