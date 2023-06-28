package com.example.defecttrackerserver.core.defect.process;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/processes")
public class ProcessController {
    private final ProcessService processService;

    @PostMapping
    public ProcessDto saveProcess(@RequestBody ProcessDto processDto) {
        return processService.saveProcess(processDto);
    }

    @GetMapping("/{id}")
    public ProcessDto getProcess(@PathVariable Integer id) {
        return processService.getProcessById(id);
    }

    @GetMapping
    public List<ProcessDto> getAllDefectTypes() {
        return processService.getAllProcesses();
    }

    @PutMapping
    public ProcessDto updateDefectType(@RequestBody ProcessDto processDto) {
        return processService.updateProcess(processDto);
    }

    @DeleteMapping("/{id}")
    public void deleteDefectType(@PathVariable Integer id) {
        processService.deleteProcess(id);
    }
}
