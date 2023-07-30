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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/actions")
@Tag(name = "Actions")
public class ActionController {
    private final ActionService actionService;

    @Operation(
            summary = "Save Action",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Action saved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
            }
    )
    @PostMapping()
    public ActionDto saveAction(@Valid @RequestBody ActionDto actionDto) {
        return actionService.saveAction(actionDto);
    }

    @Operation(
            summary = "Get Action by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Action found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Action not found"),
            }
    )
    @GetMapping("/{id}")
    public ActionDto getAction(@PathVariable Integer id) {
        return actionService.getActionById(id);
    }

    @Operation(
            summary = "Get all Actions with filter values",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Actions found"),
            }
    )
    @GetMapping("")
    public PaginatedResponse<ActionDto> getFilteredActions(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDateStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDateEnd,
            @RequestParam(required = false) Boolean isCompleted,
            @RequestParam(required = false) String assignedUserIds,
            @RequestParam(required = false) String defectIds,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtEnd,
            @RequestParam(required = false) String createdByIds,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) List<String> sort) {

        List<Integer> assignedUserIdList = (assignedUserIds != null) ? Arrays.stream(assignedUserIds.split(","))
                .map(Integer::valueOf)
                .toList() : null;

        List<Integer> defectIdList = (defectIds != null) ? Arrays.stream(defectIds.split(","))
                .map(Integer::valueOf)
                .toList() : null;

        List<Integer> createdByIdList = (createdByIds != null) ? Arrays.stream(createdByIds.split(","))
                .map(Integer::valueOf)
                .toList() : null;

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
                assignedUserIdList, defectIdList, createdAtStart, createdAtEnd, createdByIdList, pageable);
    }

    @Operation(
            summary = "Close Action",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Action found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Action not found"),
            }
    )
    @PatchMapping("/{id}")
    public void closeAction(@PathVariable Integer id, @RequestParam Boolean isCompleted) {
        actionService.closeAction(id, isCompleted);
    }

    @Operation(
            summary = "Update Action",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Action updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Action not found"),
            }
    )
    @PutMapping("/{id}")
    public ActionDto updateAction(@PathVariable Integer id, @Valid @RequestBody ActionDto actionDto) {
        return actionService.updateAction(id, actionDto);
    }

    @Operation(
            summary = "Delete Action by id",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Action successfully deleted"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Action not found"),
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAction(@PathVariable Integer id) {
        actionService.deleteAction(id);
        return ResponseEntity.noContent().build();
    }
}
