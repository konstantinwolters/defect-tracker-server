package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.response.PaginatedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/defects")
public class DefectController {
    private final DefectService defectService;

    @PostMapping()
    public DefectDto saveDefect(@Valid @RequestBody DefectDto defectDto) { return defectService.saveDefect(defectDto);}

    @GetMapping("/{id}")
    public DefectDto getDefectById(@PathVariable Integer id) { return defectService.getDefectById(id);}

    //TODO: Fix issue with sorting
    @GetMapping()
    public PaginatedResponse<DefectDto> getFilteredDefects(
            @RequestParam(required = false) String lotIds,
            @RequestParam(required = false) String defectStatusIds,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtEnd,
            @RequestParam(required = false) String locationIds,
            @RequestParam(required = false) String processIds,
            @RequestParam(required = false) String defectTypeIds,
            @RequestParam(required = false) String createdByIds,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        List<Integer> lotIdList = (lotIds != null) ? Arrays.stream(lotIds.split(","))
                .map(Integer::valueOf)
                .toList() : null;

        List<Integer> defectStatusIdList = (defectStatusIds != null) ? Arrays.stream(defectStatusIds.split(","))
                .map(Integer::valueOf)
                .toList() : null;

        List<Integer> locationIdList = (locationIds != null) ? Arrays.stream(locationIds.split(","))
                .map(Integer::valueOf)
                .toList() : null;

        List<Integer> processIdList = (processIds != null) ? Arrays.stream(processIds.split(","))
                .map(Integer::valueOf)
                .toList() : null;

        List<Integer> defectTypeIdList = (defectTypeIds != null) ? Arrays.stream(defectTypeIds.split(","))
                .map(Integer::valueOf)
                .toList() : null;

        List<Integer> createdByIdList = (createdByIds != null) ? Arrays.stream(createdByIds.split(","))
                .map(Integer::valueOf)
                .toList() : null;

        return defectService.getDefects(lotIdList, defectStatusIdList, createdAtStart, createdAtEnd, locationIdList,
                processIdList, defectTypeIdList, createdByIdList,pageable);
    }

    @PutMapping("/{id}")
    public DefectDto updateDefect(@PathVariable Integer id, @Valid @RequestBody DefectDto defectDto) {
        return defectService.updateDefect(id, defectDto); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDefect(@PathVariable Integer id) { defectService.deleteDefect(id);
        return ResponseEntity.noContent().build();}
}
