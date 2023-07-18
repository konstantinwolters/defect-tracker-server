package com.example.defecttrackerserver.core.defect.process;

import com.example.defecttrackerserver.core.defect.defectType.DefectType;
import com.example.defecttrackerserver.core.defect.defectType.DefectTypeDto;
import org.springframework.stereotype.Component;

@Component
public class ProcessMapper {
    public ProcessDto mapToDto(Process process) {
        ProcessDto processDto = new ProcessDto();
        processDto.setId(process.getId());
        processDto.setName(process.getName());
        return processDto;
    }
}
