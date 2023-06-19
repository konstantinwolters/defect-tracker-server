package com.example.defecttrackerserver.core.defect.defect;

import lombok.RequiredArgsConstructor;
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
    public DefectDto getDefect(@PathVariable Integer id) { return defectService.getDefect(id);}

    @GetMapping()
    public List<DefectDto> getAllDefects() { return defectService.getAllDefects();}

    @PutMapping()
    public DefectDto updateDefect(@RequestBody DefectDto defectDto) { return defectService.updateDefect(defectDto); }

    @DeleteMapping("/{id}")
    public void deleteDefect(@PathVariable Integer id) { defectService.deleteDefect(id);}


}
