package com.example.defecttrackerserver.core.defect.defectStatus;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/defectstatus")
public class DefectStatusController {
    private final DefectStatusService defectStatusService;

    @PostMapping
    public DefectStatusDto saveDefectStatus(@Valid @RequestBody DefectStatusDto defectStatusDto) {
        return defectStatusService.saveDefectStatus(defectStatusDto);
    }

    @GetMapping("/{id}")
    public DefectStatusDto getDefectStatus(@PathVariable Integer id) {
        return defectStatusService.getDefectStatusById(id);
    }

    @GetMapping
    public List<DefectStatusDto> getAllDefectStatus() {
        return defectStatusService.getAllDefectStatus();
    }

    @PutMapping("/{id}")
    public DefectStatusDto updateDefectStatus(@PathVariable Integer id, @Valid @RequestBody DefectStatusDto defectStatusDto) {
        return defectStatusService.updateDefectStatus(1, defectStatusDto);
    }

    @DeleteMapping("/{id}")
    public void deleteDefectStatus(@PathVariable Integer id) {
        defectStatusService.deleteDefectStatus(id);
    }
}
