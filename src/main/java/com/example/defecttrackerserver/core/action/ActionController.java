package com.example.defecttrackerserver.core.action;

import lombok.RequiredArgsConstructor;
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
    public List<ActionDto> getAllActions() {
        return actionService.getAllActions();
    }

    @GetMapping("/by-defect/{id}")
    public List<ActionDto> getAllActionsByDefectId(@PathVariable Integer id) {
        return actionService.getAllActionsByDefectId(id);
    }
}
