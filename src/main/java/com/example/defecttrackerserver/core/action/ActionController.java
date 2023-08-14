package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.response.PaginatedResponse;
import com.example.defecttrackerserver.utils.Utils;
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
    private final Utils utils;

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
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDateStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDateEnd,
            @RequestParam(required = false) Boolean isCompleted,
            @RequestParam(required = false) String assignedUserIds,
            @RequestParam(required = false) String defectIds,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtEnd,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate changedAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate changedAtEnd,
            @RequestParam(required = false) String createdByIds,
            @RequestParam(required = false) String changedByIds,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String sort) {

        List<Integer> assignedUserIdList = utils.convertStringToListOfInteger(assignedUserIds);
        List<Integer> defectIdList = utils.convertStringToListOfInteger(defectIds);
        List<Integer> createdByIdList = utils.convertStringToListOfInteger(createdByIds);
        List<Integer> changedByIdList = utils.convertStringToListOfInteger(changedByIds);

        Sort sorting = Sort.unsorted();
        if (sort != null && !sort.isEmpty()) {
            String[] split = sort.split(",");
                Sort.Direction direction = Sort.Direction.fromString(split[1]);
                sorting = Sort.by(direction, split[0]);
        }

        // After Sort object creation
        System.out.println("Sort object: " + sorting.toString());


        Pageable pageable = PageRequest.of(page, size, sorting);

        return actionService.getActions(search, dueDateStart, dueDateEnd, isCompleted, assignedUserIdList,
                defectIdList, createdAtStart, createdAtEnd, changedAtStart, changedAtEnd,
                createdByIdList, changedByIdList, pageable);
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
    public void closeAction(@PathVariable Integer id) {
        actionService.closeAction(id);
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
