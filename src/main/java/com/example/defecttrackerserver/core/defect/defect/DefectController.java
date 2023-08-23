package com.example.defecttrackerserver.core.defect.defect;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/defects")
@Tag(name = "Defects")
public class DefectController {
    private final DefectService defectService;

    @Operation(
            summary = "Save Defect",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Defect saved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DefectDto saveDefect(@Valid @RequestPart("defect") DefectDto defectDto,
                                @RequestPart("images")MultipartFile[] images) {
        return defectService.saveDefect(defectDto, images);}

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

    @Operation(
            summary = "Get all Defects with search, filter and sort values",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Defects found"),
            }
    )
    @GetMapping()
    public PaginatedResponse<DefectDto> getFilteredDefects(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String lotIds,
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

        return defectService.getDefects(search, lotIds, materialIds, supplierIds, defectStatusIds,
                causationCategoryIds, createdAtStart, createdAtEnd, changedAtStart, changedAtEnd,
                locationIds, processIds, defectTypeIds, createdByIds, changedByIds, page, size, sort);
    }

    @Operation(
            summary = "Update Defect",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Defect updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Defect not found"),
            }
    )
    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DefectDto updateDefect(@PathVariable Integer id, @Valid @RequestPart("defect") DefectDto defectDto,
                                  @RequestPart(name = "images", required = false)MultipartFile[] images) {

        return defectService.updateDefect(id, defectDto, images);
    }

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
