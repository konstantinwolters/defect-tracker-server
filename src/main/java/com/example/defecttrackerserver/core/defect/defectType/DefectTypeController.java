package com.example.defecttrackerserver.core.defect.defectType;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/defecttypes")
public class DefectTypeController {
    private final DefectTypeService defectTypeService;

    @PostMapping
    public DefectTypeDto saveDefectType(@Valid @RequestBody DefectTypeDto defectTypeDto) {
        return defectTypeService.saveDefectType(defectTypeDto);
    }

    @GetMapping("/{id}")
    public DefectTypeDto getDefectType(@PathVariable Integer id) {
        return defectTypeService.getDefectTypeById(id);
    }

    @GetMapping
    public List<DefectTypeDto> getAllDefectTypes() {
        return defectTypeService.getAllDefectTypes();
    }

    @PutMapping("/{id}")
    public DefectTypeDto updateDefectType(@PathVariable Integer id, @Valid @RequestBody DefectTypeDto defectTypeDto) {
        return defectTypeService.updateDefectType(1, defectTypeDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDefectType(@PathVariable Integer id) {
        defectTypeService.deleteDefectType(id);
        return ResponseEntity.noContent().build();
    }
}
