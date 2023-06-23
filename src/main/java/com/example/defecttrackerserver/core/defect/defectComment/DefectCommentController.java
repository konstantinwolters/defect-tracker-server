package com.example.defecttrackerserver.core.defect.defectComment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/defects")
public class DefectCommentController {

    private final DefectCommentService defectCommentService;

    @GetMapping("/comments/{id}")
    public DefectCommentDto getDefectCommentById(@PathVariable Integer id) {
        return defectCommentService.getDefectCommentById(id);
    }

    @PostMapping("/{id}/comments")
    public DefectCommentDto addDefectCommentToDefect(@PathVariable Integer id,
                                               @RequestBody DefectCommentDto defectCommentDto) {
        return defectCommentService.addDefectCommentToDefect(id, defectCommentDto);
    }

    @PutMapping("/comments")
    public DefectCommentDto updateDefectComment(@RequestBody DefectCommentDto defectCommentDto) {
        return defectCommentService.updateDefectComment(defectCommentDto);
    }

    @DeleteMapping("/{defectId}/comments/{commentId}")
    public void deleteDefectComment(@PathVariable Integer defectId, @PathVariable Integer commentId) {
        defectCommentService.deleteDefectComment(defectId, commentId);
    }
}
