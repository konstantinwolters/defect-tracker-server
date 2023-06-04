package com.example.defecttrackerserver.core.action;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/actions")
public class ActionController {
    private final ActionService actionService;

    @PostMapping()
    public ActionDto saveAction(@RequestBody ActionDto actionDto) {
        return actionService.saveAction(actionDto);
    }
}
