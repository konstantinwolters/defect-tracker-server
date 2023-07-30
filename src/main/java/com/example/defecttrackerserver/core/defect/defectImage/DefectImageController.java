package com.example.defecttrackerserver.core.defect.defectImage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/defects")
@Tag(name = "DefectImages")
public class DefectImageController {
    private final DefectImageService defectImageService;

    @Operation(
            summary = "Add a DefectImage to a Defect",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DefectImage saved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Defect not found"),
            }
    )
    @PostMapping("/{defectId}/images")
    public DefectImageDto addImageToDefect(@PathVariable Integer defectId, @Valid @RequestBody DefectImageDto defectImageDto) {
        return defectImageService.saveDefectImageToDefect(defectId, defectImageDto);
    }

    @Operation(
            summary = "Get DefectImage by Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DefectImage found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "DefectImage not found"),
            }
    )
    @GetMapping("/images/{id}")
    public DefectImageDto getImageById(@PathVariable Integer id) {
        return defectImageService.getDefectImageById(id);
    }

    @Operation(
            summary = "Update DefectImage",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DefectImage updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Defect or DefectImage not found"),
            }
    )
    @PutMapping("/images/{id}")
    public DefectImageDto updateImage(@PathVariable Integer id, @Valid @RequestBody DefectImageDto defectImageDto) {
        return defectImageService.updateDefectImage(id, defectImageDto);
    }

    @Operation(
            summary = "Delete DefectImage",
            responses = {
                    @ApiResponse(responseCode = "204", description = "DefectImage successfully deleted"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Defect or DefectImage not found"),
            }
    )
    @DeleteMapping("/{defectId}/images/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Integer defectId, @PathVariable Integer imageId) {
        defectImageService.deleteDefectImage(defectId, imageId);
        return ResponseEntity.noContent().build();
    }
}
