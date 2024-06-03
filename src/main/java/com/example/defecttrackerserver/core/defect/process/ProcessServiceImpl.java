package com.example.defecttrackerserver.core.defect.process;

import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of {@link Process}.
 */
@Service
@RequiredArgsConstructor
class ProcessServiceImpl implements ProcessService {
    private final ProcessRepository processRepository;
    private final DefectRepository defectRepository;
    private final ProcessMapper processMapper;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ProcessDto saveProcess(ProcessDto processDto) {
        if(processRepository.findByName(processDto.getName()).isPresent())
            throw new DuplicateKeyException("Process already exists with name: " + processDto.getName());

        Process process = new Process();
        process.setName(processDto.getName());
        process.setCustomId(processDto.getCustomId());

        Process savedProcess = processRepository.save(process);

        return processMapper.mapToDto(savedProcess);
    }

    @Override
    public ProcessDto getProcessById(Integer id) {
        Process process = findProcessById(id);
        return processMapper.mapToDto(process);
    }

    @Override
    public List<ProcessDto> getAllProcesses() {
        return processRepository.findAll()
                .stream()
                .map(processMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ProcessDto updateProcess(Integer processId, ProcessDto processDto) {
        Process process = findProcessById(processId);

        Optional<Process> processExists = processRepository.findByName(processDto.getName());
        if(processExists.isPresent() && !processExists.get().getId().equals(process.getId()))
            throw new DuplicateKeyException("Process already exists with name: " + processDto.getName());

        process.setName(processDto.getName());
        process.setCustomId(processDto.getCustomId());

        Process savedProcess = processRepository.save(process);

        return processMapper.mapToDto(savedProcess);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteProcess(Integer id) {
        Process process = findProcessById(id);

        if(!defectRepository.findByProcessId(id).isEmpty())
            throw new UnsupportedOperationException("Process cannot be deleted because it is used in Defects");

        processRepository.delete(process);
    }

    private Process findProcessById(Integer id) {
        return processRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Process not found with id: " + id));
    }
}
