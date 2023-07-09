package com.example.defecttrackerserver.core.action;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/actions")
public class ActionController {
    private final ActionService actionService;

    @PostMapping()
    public ActionDto saveAction(@RequestBody ActionDto actionDto) {
        return actionService.saveAction(actionDto);
    }

    @GetMapping("/{id}")
    public ActionDto getAction(@PathVariable Integer id) {
        return actionService.getActionById(id);
    }

    @GetMapping()
    public List<ActionDto> getAllActions() { return actionService.getAllActions(); }

    @GetMapping("/filtered")
    public List<ActionDto> getFilteredActions(
            @RequestParam(required = false) String dueDateStart,
            @RequestParam(required = false) String dueDateEnd,
            @RequestParam(required = false) Boolean isCompleted,
            @RequestParam(required = false) List<Integer> assignedUserIds,
            @RequestParam(required = false) List<Integer> defectIds,
            @RequestParam(required = false) String createdOnStart,
            @RequestParam(required = false) String createdOnEnd,
            @RequestParam(required = false) List<Integer> createdByIds) {
        return actionService.getFilteredActions(dueDateStart, dueDateEnd, isCompleted,
                assignedUserIds, defectIds, createdOnStart, createdOnEnd, createdByIds);
    }

    @PutMapping("/{id}")
    public void closeAction(@PathVariable Integer id, @RequestParam Boolean isCompleted) {
        actionService.closeAction(id, isCompleted);
    }

    @PutMapping()
    public ActionDto updateAction(@RequestBody ActionDto actionDto) {
        return actionService.updateAction(actionDto);
    }

    @DeleteMapping("/{id}")
    public void deleteAction(@PathVariable Integer id) {
        actionService.deleteAction(id);
    }
}
