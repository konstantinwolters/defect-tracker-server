package com.example.defecttrackerserver.core.defect.process;

import org.springframework.stereotype.Component;

/**
 * Provides methods to map entity objects to DTOs and vice versa.
 */
@Component
public class ProcessMapper {
    public ProcessDto mapToDto(Process process) {
        ProcessDto processDto = new ProcessDto();
        processDto.setId(process.getId());
        processDto.setName(process.getName());
        processDto.setCustomId(process.getCustomId());
        return processDto;
    }
}
