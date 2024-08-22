package com.solution.calc.utils;

import java.time.LocalDate;

public class IndexUtils {

    public static String getDateKey(LocalDate date, Long userId) {
        return date.toString() + "-" + userId;
    }
}
