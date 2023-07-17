package com.example.defecttrackerserver.core.defect.process;

import com.example.defecttrackerserver.core.defect.process.processException.processExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProcessServiceImpl implements ProcessService {
    private final ProcessRepository processRepository;
    private final ProcessMapper processMapper;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ProcessDto saveProcess(ProcessDto processDto) {
        if(processDto.getName() == null)
            throw new IllegalArgumentException("Process name must not be null");

        if(processRepository.findByName(processDto.getName()).isPresent())
            throw new processExistsException("Process already exists with name: " + processDto.getName());

        Process process = new Process();
        process.setName(processDto.getName());

        Process savedProcess = processRepository.save(process);

        return processMapper.mapToDto(savedProcess);
    }

    @Override
    public ProcessDto getProcessById(Integer id) {
        return processRepository.findById(id)
                .map(processMapper::mapToDto)
                .orElseThrow(() -> new IllegalArgumentException("Process not found with id: " + id));
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
    public ProcessDto updateProcess(ProcessDto processDto) {
        if(processDto.getId() == null)
            throw new IllegalArgumentException("Process id must not be null");
        if(processDto.getName() == null)
            throw new IllegalArgumentException("Process name must not be null");

        Process process = processRepository.findById(processDto.getId())
                .orElseThrow(()-> new EntityNotFoundException("Process not found with id: "
                        + processDto.getId()));

        Optional<Process> processExists = processRepository.findByName(processDto.getName());
        if(processExists.isPresent() && !processExists.get().getId().equals(process.getId()))
            throw new processExistsException("Process already exists with name: " + processDto.getName());

        process.setName(processDto.getName());
        Process savedProcess = processRepository.save(process);

        return processMapper.mapToDto(savedProcess);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteProcess(Integer id) {
        Process process = processRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Process not found with id: " + id));

        processRepository.delete(process);
    }
}
