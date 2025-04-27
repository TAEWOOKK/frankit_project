package com.app.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {

    public static LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime getSearchStartDateTime(LocalDate localDate) {
        return LocalDateTime.of(localDate, LocalTime.MIN);
    }

    public static LocalDateTime getSearchEndDateTime(LocalDate localDate) {
        return LocalDateTime.of(localDate, LocalTime.MAX);
    }
}
