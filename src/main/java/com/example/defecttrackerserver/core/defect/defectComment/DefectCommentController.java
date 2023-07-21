package com.example.defecttrackerserver.core.defect.defectComment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/defects")
public class DefectCommentController {

    private final DefectCommentService defectCommentService;

    @PostMapping("/{id}/comments")
    public DefectCommentDto addDefectCommentToDefect(@PathVariable Integer id,
                                                     @Valid @RequestBody DefectCommentDto defectCommentDto) {
        return defectCommentService.addDefectCommentToDefect(id, defectCommentDto);
    }

    @GetMapping("/comments/{id}")
    public DefectCommentDto getDefectCommentById(@PathVariable Integer id) {
        return defectCommentService.getDefectCommentById(id);
    }

    @PutMapping("/comments/{id}")
    public DefectCommentDto updateDefectComment(@PathVariable Integer id, @Valid @RequestBody DefectCommentDto defectCommentDto) {
        return defectCommentService.updateDefectComment(id, defectCommentDto);
    }

    @DeleteMapping("/{defectId}/comments/{commentId}")
    public ResponseEntity<Void> deleteDefectComment(@PathVariable Integer defectId, @PathVariable Integer commentId) {
        defectCommentService.deleteDefectComment(defectId, commentId);
        return ResponseEntity.noContent().build();
    }
}
