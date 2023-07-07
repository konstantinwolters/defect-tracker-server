package com.example.defecttrackerserver.core.defect.defect;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/defects")
public class DefectController {
    private final DefectService defectService;

    @PostMapping()

    public DefectDto saveDefect(@RequestBody DefectDto defectDto) { return defectService.saveDefect(defectDto);}

    @GetMapping("/{id}")
    public DefectDto getDefectById(@PathVariable Integer id) { return defectService.getDefectById(id);}

    @GetMapping()
    public List<DefectDto> getAllDefects() { return defectService.getAllDefects();}

    @GetMapping("/filtered")
    public List<DefectDto> getFilteredDefects(
            @RequestParam(required = false) List<Integer> lotIds,
            @RequestParam(required = false) List<Integer> defectStatusIds,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) List<Integer> locationIds,
            @RequestParam(required = false) List<Integer> processIds,
            @RequestParam(required = false) List<Integer> defectTypeIds,
            @RequestParam(required = false) List<Integer> createdByIds) {

        return defectService.getFilteredDefects(lotIds, defectStatusIds, startDate, endDate,
                locationIds, processIds, defectTypeIds, createdByIds);
    }

    @PutMapping()
    public DefectDto updateDefect(@RequestBody DefectDto defectDto) { return defectService.updateDefect(defectDto); }

    @DeleteMapping("/{id}")
    public void deleteDefect(@PathVariable Integer id) { defectService.deleteDefect(id);}
}
