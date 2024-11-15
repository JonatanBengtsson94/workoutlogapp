package com.jonatanbengtsson.workoutlog.utils;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class DateUtil {

    public static String getFormattedDate(LocalDate date) {
       LocalDate today = LocalDate.now();
       long daysBetween = ChronoUnit.DAYS.between(date, today);

       if (daysBetween == 0) {
           return "Today";
       } else if (daysBetween == 1) {
           return "Yesterday";
       } else if (daysBetween <= 7) {
           return "Last " + date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
       }

       return date.toString();
    }
}
