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

    @GetMapping("/created-by/{id}")
    public List<ActionDto> getAllActionsCreatedBy(@PathVariable Integer id) {
        return actionService.getAllActionsByUserCreatedId(id);
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
