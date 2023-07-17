package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.response.PaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("")
    public PaginatedResponse<ActionDto> getFilteredActions(
            @RequestParam(required = false) String dueDateStart,
            @RequestParam(required = false) String dueDateEnd,
            @RequestParam(required = false) Boolean isCompleted,
            @RequestParam(required = false) List<Integer> assignedUserIds,
            @RequestParam(required = false) List<Integer> defectIds,
            @RequestParam(required = false) String createdOnStart,
            @RequestParam(required = false) String createdOnEnd,
            @RequestParam(required = false) List<Integer> createdByIds,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) List<String> sort) {

        Sort sorting = sort == null ?
                Sort.unsorted() :
                sort.stream()
                        .map(sortStr -> {
                            String[] split = sortStr.split(",");
                            return Sort.by(Sort.Direction.fromString(split[1]), split[0]);
                        })
                        .reduce(Sort.unsorted(), Sort::and);

        Pageable pageable = PageRequest.of(page, size, sorting);

        return actionService.getActions(dueDateStart, dueDateEnd, isCompleted,
                assignedUserIds, defectIds, createdOnStart, createdOnEnd, createdByIds, pageable);
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
