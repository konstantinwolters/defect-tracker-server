package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.response.PaginatedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping()
    public PaginatedResponse<DefectDto> getFilteredDefects(
            @RequestParam(required = false) List<Integer> lotIds,
            @RequestParam(required = false) List<Integer> defectStatusIds,
            @RequestParam(required = false) String createdOnStart,
            @RequestParam(required = false) String createdOnEnd,
            @RequestParam(required = false) List<Integer> locationIds,
            @RequestParam(required = false) List<Integer> processIds,
            @RequestParam(required = false) List<Integer> defectTypeIds,
            @RequestParam(required = false) List<Integer> createdByIds,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        return defectService.getDefects(lotIds, defectStatusIds, createdOnStart, createdOnEnd,
                locationIds, processIds, defectTypeIds, createdByIds, pageable);
    }

    @PutMapping("/{id}")
    public DefectDto updateDefect(@PathVariable Integer id, @Valid @RequestBody DefectDto defectDto) {
        return defectService.updateDefect(id, defectDto); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDefect(@PathVariable Integer id) { defectService.deleteDefect(id);
        return ResponseEntity.noContent().build();}
}
