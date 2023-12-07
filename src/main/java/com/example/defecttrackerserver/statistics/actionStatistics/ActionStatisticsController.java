package com.example.defecttrackerserver.statistics.actionStatistics;

import com.example.defecttrackerserver.statistics.defectStatistics.DefectStatisticsDto;
import com.example.defecttrackerserver.statistics.defectStatistics.DefectStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ActionStatisticsController {
    private final ActionStatisticsService actionStatisticsService;

    @GetMapping("/actions/statistics")
    public ActionStatisticsDto getActionStatistics(){
        return actionStatisticsService.getActionStatistics();
    }
}
