package com.example.defecttrackerserver.core.defect.process;

import java.util.List;

public interface ProcessService {
    ProcessDto saveProcess(ProcessDto processDto);
    ProcessDto getProcessById(Integer id);
    List<ProcessDto> getAllProcesses();
    ProcessDto updateProcess(Integer processId, ProcessDto processDto);
    void deleteProcess(Integer id);
}
