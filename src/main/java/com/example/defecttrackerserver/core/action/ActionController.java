package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.response.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/actions")
@Tag(name = "Actions")
public class ActionController {
    private final ActionService actionService;

    @Operation(
            description = "Save new action",
            summary = "Saves actions",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Action saved successfully"),
            }
    )
    @PostMapping()
    public ActionDto saveAction(@Valid @RequestBody ActionDto actionDto) {
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

    @PatchMapping("/{id}")
    public void closeAction(@PathVariable Integer id, @RequestParam Boolean isCompleted) {
        actionService.closeAction(id, isCompleted);
    }

    @PutMapping("/{id}")
    public ActionDto updateAction(@PathVariable Integer id, @Valid @RequestBody ActionDto actionDto) {
        return actionService.updateAction(id, actionDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAction(@PathVariable Integer id) {
        actionService.deleteAction(id);
        return ResponseEntity.noContent().build();
    }
}
