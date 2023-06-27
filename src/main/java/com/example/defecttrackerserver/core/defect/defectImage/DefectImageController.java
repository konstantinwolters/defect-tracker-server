package com.example.defecttrackerserver.core.defect.defectImage;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/defects")
public class DefectImageController {
    private final DefectImageService defectImageService;

    @PostMapping("/{defectId}/images")
    public DefectImageDto addImageToDefect(@PathVariable Integer defectId, @RequestBody DefectImageDto defectImageDto) {
        return defectImageService.saveDefectImageToDefect(defectId, defectImageDto);
    }

    @GetMapping("/images/{id}")
    public DefectImageDto getImageById(@PathVariable Integer id) {
        return defectImageService.getDefectImageById(id);
    }

    @PutMapping("/images")
    public DefectImageDto updateImage(@RequestBody DefectImageDto defectImageDto) {
        return defectImageService.updateDefectImage(defectImageDto);
    }

    @DeleteMapping("/{defectId}/images/{imageId}")
    public void deleteImage(@PathVariable Integer defectId, @PathVariable Integer imageId) {
        defectImageService.deleteDefectImageById(defectId, imageId);
    }
}
