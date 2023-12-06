package com.example.defecttrackerserver.statistics.actionStatistics;

import com.example.defecttrackerserver.statistics.IsCompletedCount;
import com.example.defecttrackerserver.statistics.YearAndMonthCounts;
import com.example.defecttrackerserver.statistics.YearCount;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ActionStatisticsDto {

    private List<YearAndMonthCounts> dueDateYearAndMonthCounts;
    private List<YearCount> dueDateYearCounts;
    private IsCompletedCount isCompletedCounts;
    private List<YearAndMonthCounts> createdAtYearAndMonthCounts;
    private List<YearCount> createdAtYearCounts;
}
