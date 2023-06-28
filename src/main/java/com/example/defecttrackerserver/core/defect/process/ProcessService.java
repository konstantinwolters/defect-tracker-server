package com.example.defecttrackerserver.core.defect.process;

import com.example.defecttrackerserver.core.defect.defectType.DefectTypeDto;

import java.util.List;

public interface ProcessService {
    ProcessDto saveProcess(ProcessDto processDto);
    ProcessDto getProcessById(Integer id);
    List<ProcessDto> getAllProcesses();
    ProcessDto updateProcess(ProcessDto processDto);
    void deleteProcess(Integer id);
}
