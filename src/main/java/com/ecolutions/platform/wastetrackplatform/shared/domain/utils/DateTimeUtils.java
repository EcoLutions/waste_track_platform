package com.ecolutions.platform.wastetrackplatform.shared.domain.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;

public final class DateTimeUtils {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;

    private DateTimeUtils() {}

    public static String yearToStringOrNull(Year year) {
        return year != null ? year.toString() : null;
    }

    public static Year stringToYearOrNull(String yearString) {
        if (yearString == null || yearString.isBlank()) {
            return null;
        }
        try {
            return Year.parse(yearString);
        } catch (Exception e) {
            return null;
        }
    }

    public static String localDateToStringOrNull(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : null;
    }

    public static LocalDate stringToLocalDateOrNull(String dateString) {
        if (dateString == null || dateString.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    public static String localDateTimeToStringOrNull(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATETIME_FORMATTER) : null;
    }

    public static LocalDateTime stringToLocalDateTimeOrNull(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.isBlank()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeString, DATETIME_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    public static String localTimeToStringOrNull(LocalTime time) {
        return time != null ? time.format(TIME_FORMATTER) : null;
    }

    public static LocalTime stringToLocalTimeOrNull(String timeString) {
        if (timeString == null || timeString.isBlank()) {
            return null;
        }
        try {
            return LocalTime.parse(timeString, TIME_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }
}
