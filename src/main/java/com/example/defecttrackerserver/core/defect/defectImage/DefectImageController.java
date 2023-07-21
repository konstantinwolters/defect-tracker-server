package com.example.defecttrackerserver.core.defect.defectImage;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/defects")
public class DefectImageController {
    private final DefectImageService defectImageService;

    @PostMapping("/{defectId}/images")
    public DefectImageDto addImageToDefect(@PathVariable Integer defectId, @Valid @RequestBody DefectImageDto defectImageDto) {
        return defectImageService.saveDefectImageToDefect(defectId, defectImageDto);
    }

    @GetMapping("/images/{id}")
    public DefectImageDto getImageById(@PathVariable Integer id) {
        return defectImageService.getDefectImageById(id);
    }

    @PutMapping("/images/{id}")
    public DefectImageDto updateImage(@PathVariable Integer id, @Valid @RequestBody DefectImageDto defectImageDto) {
        return defectImageService.updateDefectImage(id, defectImageDto);
    }

    @DeleteMapping("/{defectId}/images/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Integer defectId, @PathVariable Integer imageId) {
        defectImageService.deleteDefectImageFromDefect(defectId, imageId);
        return ResponseEntity.noContent().build();
    }
}
