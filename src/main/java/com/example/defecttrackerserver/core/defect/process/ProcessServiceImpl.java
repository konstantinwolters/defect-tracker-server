package com.example.defecttrackerserver.core.defect.process;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProcessServiceImpl implements ProcessService {
    private final ProcessRepository processRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProcessDto saveProcess(ProcessDto processDto) {
        if(processDto.getName() == null)
            throw new IllegalArgumentException("Process name must not be null");

        Process process = new Process();
        process.setName(processDto.getName());

        Process savedProcess = processRepository.save(process);

        return modelMapper.map(savedProcess, ProcessDto.class);
    }

    @Override
    public ProcessDto getProcessById(Integer id) {
        return processRepository.findById(id)
                .map(process -> modelMapper.map(process, ProcessDto.class))
                .orElseThrow(() -> new IllegalArgumentException("Process not found with id: " + id));
    }

    @Override
    public List<ProcessDto> getAllProcesses() {
        return processRepository.findAll()
                .stream()
                .map(process -> modelMapper.map(process, ProcessDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProcessDto updateProcess(ProcessDto processDto) {
        if(processDto.getId() == null)
            throw new IllegalArgumentException("Process id must not be null");
        if(processDto.getName() == null)
            throw new IllegalArgumentException("Process name must not be null");

        Process process = processRepository.findById(processDto.getId())
                .orElseThrow(()-> new EntityNotFoundException("Process not found with id: "
                        + processDto.getId()));

        process.setName(processDto.getName());
        Process savedProcess = processRepository.save(process);

        return modelMapper.map(savedProcess, ProcessDto.class);
    }

    @Override
    public void deleteProcess(Integer id) {
        processRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Process not found with id: " + id));

        processRepository.deleteById(id);
    }
}
