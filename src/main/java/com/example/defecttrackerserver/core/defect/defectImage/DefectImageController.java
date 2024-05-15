package com.example.defecttrackerserver.core.defect.defectImage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller for managing {@link DefectImage}.
 * Provides endpoints for creating, updating, deleting, and retrieving defect images.
 */
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
    public DefectImageDto addImageToDefect(@PathVariable Integer defectId, @RequestParam("image") MultipartFile image) {
        return defectImageService.saveDefectImageToDefect(defectId, image);
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
            summary = "Get DefectImage URL by UUID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DefectImage URL found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "DefectImage not found"),
            }
    )
    @GetMapping("/images/{uuid}/url")
    public String getDefectImageUrl(@PathVariable String uuid) {
        return defectImageService.getDefectImageUrl(uuid);
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
