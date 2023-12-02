package com.example.defecttrackerserver.statistics;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class YearAndMonthCounts {
    private int year;
    private int month;
    private Long count;
}
