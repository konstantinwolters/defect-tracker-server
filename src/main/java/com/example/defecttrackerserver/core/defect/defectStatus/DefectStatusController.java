package com.example.defecttrackerserver.core.defect.defectStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/defectstatus")
public class DefectStatusController {
    private final DefectStatusService defectStatusService;

    @PostMapping
    public DefectStatusDto saveDefectStatus(@RequestBody DefectStatusDto defectStatusDto) {
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

    @PutMapping
    public DefectStatusDto updateDefectStatus(@RequestBody DefectStatusDto defectStatusDto) {
        return defectStatusService.updateDefectStatus(defectStatusDto);
    }

    @DeleteMapping("/{id}")
    public void deleteDefectStatus(@PathVariable Integer id) {
        defectStatusService.deleteDefectStatus(id);
    }
}
