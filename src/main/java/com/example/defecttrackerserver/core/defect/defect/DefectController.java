package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.core.lot.lot.Lot;
import com.example.defecttrackerserver.core.lot.lot.LotRepository;
import com.example.defecttrackerserver.core.lot.material.Material;
import com.example.defecttrackerserver.core.lot.material.MaterialRepository;
import com.example.defecttrackerserver.core.lot.supplier.Supplier;
import com.example.defecttrackerserver.core.lot.supplier.SupplierRepository;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/defects")
@Tag(name = "Defects")
public class DefectController {
    private final DefectService defectService;
    private final Utils utils;

    @Operation(
            summary = "Save Defect",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Defect saved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
            }
    )
    @PostMapping()
    public DefectDto saveDefect(@Valid @RequestBody DefectDto defectDto) { return defectService.saveDefect(defectDto);}

    @Operation(
            summary = "Get Defect by Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Defect found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Defect not found"),
            }
    )
    @GetMapping("/{id}")
    public DefectDto getDefectById(@PathVariable Integer id) { return defectService.getDefectById(id);}

    //TODO: Fix issue with sorting
    @Operation(
            summary = "Get all Defects with filter values",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Defects found"),
            }
    )
    @GetMapping()
    public PaginatedResponse<DefectDto> getFilteredDefects(
            @RequestParam(required = false) String lotsIds,
            @RequestParam(required = false) String materialIds,
            @RequestParam(required = false) String supplierIds,
            @RequestParam(required = false) String defectStatusIds,
            @RequestParam(required = false) String causationCategoryIds,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtEnd,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate changedAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate changedAtEnd,
            @RequestParam(required = false) String locationIds,
            @RequestParam(required = false) String processIds,
            @RequestParam(required = false) String defectTypeIds,
            @RequestParam(required = false) String createdByIds,
            @RequestParam(required = false) String changedByIds,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String sort) {

        List<Integer> lotIdList = utils.convertStringToListOfInteger(lotsIds);
        List<Integer> materialIdList = utils.convertStringToListOfInteger(materialIds);
        List<Integer> supplierIdList = utils.convertStringToListOfInteger(supplierIds);
        List<Integer> defectStatusIdList = utils.convertStringToListOfInteger(defectStatusIds);
        List<Integer> causationCategoryIdList = utils.convertStringToListOfInteger(causationCategoryIds);
        List<Integer> locationIdList = utils.convertStringToListOfInteger(locationIds);
        List<Integer> processIdList = utils.convertStringToListOfInteger(processIds);
        List<Integer> defectTypeIdList = utils.convertStringToListOfInteger(defectTypeIds);
        List<Integer> createdByIdList = utils.convertStringToListOfInteger(createdByIds);
        List<Integer> changedByIdList = utils.convertStringToListOfInteger(changedByIds);

        Sort sorting = Sort.unsorted();
        if (sort != null && !sort.isEmpty()) {
            String[] split = sort.split(",");
            Sort.Direction direction = Sort.Direction.fromString(split[1]);
            sorting = Sort.by(direction, split[0]);
        }

        Pageable pageable = PageRequest.of(page, size, sorting);

        return defectService.getDefects(lotIdList, materialIdList, supplierIdList, defectStatusIdList,
                causationCategoryIdList, createdAtStart, createdAtEnd, changedAtStart, changedAtEnd,
                locationIdList, processIdList, defectTypeIdList, createdByIdList, changedByIdList, pageable);
    }

    @Operation(
            summary = "Update Defect",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Defect updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Defect not found"),
            }
    )
    @PutMapping("/{id}")
    public DefectDto updateDefect(@PathVariable Integer id, @Valid @RequestBody DefectDto defectDto) {
        return defectService.updateDefect(id, defectDto); }

    @Operation(
            summary = "Delete Defect by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Defect found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Defect not found"),
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDefect(@PathVariable Integer id) { defectService.deleteDefect(id);
        return ResponseEntity.noContent().build();}
}
