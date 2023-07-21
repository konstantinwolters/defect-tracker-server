package com.example.defecttrackerserver.core.defect.process;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/processes")
public class ProcessController {
    private final ProcessService processService;

    @PostMapping
    public ProcessDto saveProcess(@Valid @RequestBody ProcessDto processDto) {
        return processService.saveProcess(processDto);
    }

    @GetMapping("/{id}")
    public ProcessDto getProcess(@PathVariable Integer id) {
        return processService.getProcessById(id);
    }

    @GetMapping
    public List<ProcessDto> getAllProcesses() {
        return processService.getAllProcesses();
    }

    @PutMapping("/{id}")
    public ProcessDto updateProcess(@PathVariable Integer id, @Valid @RequestBody ProcessDto processDto) {
        return processService.updateProcess(1, processDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcess(@PathVariable Integer id) {
        processService.deleteProcess(id);
        return ResponseEntity.noContent().build();
    }
}
