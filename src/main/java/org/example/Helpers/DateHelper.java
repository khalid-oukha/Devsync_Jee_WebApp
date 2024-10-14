package org.example.Helpers;

import java.time.LocalDate;

public class DateHelper {

    public static boolean isValidDateRange(LocalDate start_date, LocalDate end_date) {
        return start_date.isAfter(LocalDate.now().plusDays(3)) && end_date.isAfter(start_date);
    }

    public static boolean endDateIsNow(LocalDate end_date) {
        return end_date.isAfter(LocalDate.now()) || end_date.isEqual(LocalDate.now());
    }


}
