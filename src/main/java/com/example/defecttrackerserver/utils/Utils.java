package com.example.defecttrackerserver.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Component
public class Utils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public LocalDateTime convertToDateTime(String date) {
        LocalDate dateObj = LocalDate.parse(date, FORMATTER);
        return dateObj.atStartOfDay();
    }

    public List<Integer> convertStringToListOfInteger(String string) {
        return (string != null) ? Arrays.stream(string.split(","))
                .map(Integer::valueOf)
                .toList() : null;
    }
}