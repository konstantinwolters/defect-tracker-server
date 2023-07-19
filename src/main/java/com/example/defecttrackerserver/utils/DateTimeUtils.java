package com.example.defecttrackerserver.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private DateTimeUtils() {}

    public static LocalDateTime convertToDateTime(String date) {
        LocalDate dateObj = LocalDate.parse(date, FORMATTER);
        return dateObj.atStartOfDay();
    }
}