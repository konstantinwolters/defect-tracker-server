package com.example.defecttrackerserver.core.lot.material;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/materials")
public class MaterialController {
    private final MaterialService materialService;

    @PostMapping
    public MaterialDto saveProcess(@Valid @RequestBody MaterialDto materialDto) {
        return materialService.saveMaterial(materialDto);
    }

    @GetMapping("/{id}")
    public MaterialDto getProcess(@PathVariable Integer id) {
        return materialService.getMaterialById(id);
    }

    @GetMapping
    public List<MaterialDto> getAllProcesses() {
        return materialService.getAllMaterials();
    }

    @PutMapping("/{id}")
    public MaterialDto updateProcess(@PathVariable Integer id, @Valid @RequestBody MaterialDto materialDto) {
        return materialService.updateMaterial(id, materialDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcess(@PathVariable Integer id) {
        materialService.deleteMaterial(id);
        return ResponseEntity.noContent().build();
    }
}
