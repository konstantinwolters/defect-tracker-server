package com.example.defecttrackerserver.core.defect.defectComment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/defects")
public class DefectCommentController {

    private final DefectCommentService defectCommentService;

    @Operation(
            summary = "Add a DefectComment to a Defect",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DefectComment saved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Defect or DefectComment not found"),
            }
    )
    @PostMapping("/{id}/comments")
    public DefectCommentDto addDefectCommentToDefect(@PathVariable Integer id,
                                                     @Valid @RequestBody DefectCommentDto defectCommentDto) {
        return defectCommentService.addDefectCommentToDefect(id, defectCommentDto);
    }

    @Operation(
            summary = "Get DefectComment by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DefectComment found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "DefectComment not found"),
            }
    )
    @GetMapping("/comments/{id}")
    public DefectCommentDto getDefectCommentById(@PathVariable Integer id) {
        return defectCommentService.getDefectCommentById(id);
    }

    @Operation(
            summary = "Update DefectComment",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DefectComment updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "DefectComment not found"),
            }
    )
    @PutMapping("/comments/{id}")
    public DefectCommentDto updateDefectComment(@PathVariable Integer id, @Valid @RequestBody DefectCommentDto defectCommentDto) {
        return defectCommentService.updateDefectComment(id, defectCommentDto);
    }

    @Operation(
            summary = "Delete DefectComment by id",
            responses = {
                    @ApiResponse(responseCode = "204", description = "DefectComment successfully deleted"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Defect or DefectComment not found"),
            }
    )
    @DeleteMapping("/{defectId}/comments/{commentId}")
    public ResponseEntity<Void> deleteDefectComment(@PathVariable Integer defectId, @PathVariable Integer commentId) {
        defectCommentService.deleteDefectComment(defectId, commentId);
        return ResponseEntity.noContent().build();
    }
}
