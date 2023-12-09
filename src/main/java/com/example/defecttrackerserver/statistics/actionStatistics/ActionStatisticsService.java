package com.example.defecttrackerserver.statistics.actionStatistics;

import com.example.defecttrackerserver.statistics.IsCompletedCount;
import com.example.defecttrackerserver.statistics.YearAndMonthCounts;
import com.example.defecttrackerserver.statistics.YearCount;
import com.example.defecttrackerserver.statistics.YearMonthPair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ActionStatisticsService {
    private final ActionStatisticsRepository actionStatisticsRepository;

    /**
     * This method is responsible for gathering and calculating various statistics related to actions.
     * It fetches distinct year and month pairs for both created and due dates, as well as distinct years for both.
     * It then calculates counts for each year and month pair, and for each year, for both created and due dates.
     * These counts are then set in an ActionStatisticsDto object, which is returned.
     *
     * @return ActionStatisticsDto object containing various calculated statistics.
     */
    public ActionStatisticsDto getActionStatistics() {
        // Fetch distinct year and month pairs for created and due dates
        List<YearMonthPair> createdAtYearMonthPairs = actionStatisticsRepository.findCreatedAtDistinctYearAndMonth();
        List<YearMonthPair> dueDateYearMonthPairs = actionStatisticsRepository.findDueDateDistinctYearAndMonth();

        // Fetch distinct years for created and due dates
        List<Integer> createdAtYears = actionStatisticsRepository.findCreatedAtDistinctYears();
        List<Integer> dueDateYears = actionStatisticsRepository.findDueDateDistinctYears();

        // Calculate counts for each year and month pair for created and due dates
        List<YearAndMonthCounts> createdYearAndMonthAtCounts =
                calculateMonthAndYearCounts(createdAtYearMonthPairs, actionStatisticsRepository::countCreatedAtByYearAndMonth);
        List<YearAndMonthCounts> dueDateYearAndMonthCounts =
                calculateMonthAndYearCounts(dueDateYearMonthPairs, actionStatisticsRepository::countDueDateByYearAndMonth);

        // Calculate counts for each year for created and due dates
        List<YearCount> createdAtYearCounts = calculateYearCounts(createdAtYears, actionStatisticsRepository::countCreatedAtByYear);
        List<YearCount> dueDateYearCounts = calculateYearCounts(dueDateYears, actionStatisticsRepository::countDueDateByYear);

        ActionStatisticsDto actionStatistics = new ActionStatisticsDto();
        actionStatistics.setIsCompletedCounts(calculateIsCompletedCounts());
        actionStatistics.setCreatedAtYearAndMonthCounts(createdYearAndMonthAtCounts);
        actionStatistics.setCreatedAtYearCounts(createdAtYearCounts);
        actionStatistics.setDueDateYearAndMonthCounts(dueDateYearAndMonthCounts);
        actionStatistics.setDueDateYearCounts(dueDateYearCounts);

        return actionStatistics;
    }

    private IsCompletedCount calculateIsCompletedCounts() {
        Long completedCount = actionStatisticsRepository.countByIsCompleted(true);
        Long notCompletedCount = actionStatisticsRepository.countByIsCompleted(false);
        IsCompletedCount isCompletedCount = new IsCompletedCount();
        isCompletedCount.setCompletedCount(completedCount);
        isCompletedCount.setNotCompletedCount(notCompletedCount);
        return isCompletedCount;
    }

    private List<YearAndMonthCounts> calculateMonthAndYearCounts(List<YearMonthPair> yearMonthPairs, BiFunction<Integer, Integer, Long> countFunction) {
        return yearMonthPairs.stream().map(yearMonthPair -> {
            Long count = countFunction.apply(yearMonthPair.getYear(), yearMonthPair.getMonth());
            YearAndMonthCounts yearAndMonthCounts = new YearAndMonthCounts();
            yearAndMonthCounts.setYear(yearMonthPair.getYear());
            yearAndMonthCounts.setMonth(yearMonthPair.getMonth());
            yearAndMonthCounts.setCount(count);
            return yearAndMonthCounts;
        }).toList();
    }

    private List<YearCount> calculateYearCounts(List<Integer> years, Function<Integer, Long> countFunction) {
        return years.stream().map(year -> {
            Long count = countFunction.apply(year);
            YearCount yearCount = new YearCount();
            yearCount.setYear(year);
            yearCount.setCount(count);
            return yearCount;
        }).toList();
    }
}
