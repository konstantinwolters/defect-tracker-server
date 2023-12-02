package com.example.defecttrackerserver.statistics.defectStatistics;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DefectStatisticsController {
    private final DefectStatisticsService defectStatisticsService;

    @GetMapping("/defects/statistics")
    public DefectStatisticsDto getDefectStatistics(){
        return defectStatisticsService.getDefectStatistics();
    }
}
