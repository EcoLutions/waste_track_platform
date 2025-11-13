package com.ecolutions.platform.wastetrackplatform.shared.domain.utils;

import java.time.Duration;

public class DurationUtils {
    private DurationUtils() {}

    public static String durationToStringOrNull(Duration duration) {
        return duration != null ? duration.toString() : null;
    }

    public static Duration stringToDurationOrNull(String durationString) {
        if (durationString == null || durationString.isBlank()) {
            return null;
        }
        try {
            return Duration.parse(durationString);
        } catch (Exception e) {
            return null;
        }
    }

    public static long durationToSecondsOrZero(Duration duration) {
        return duration != null ? duration.toSeconds() : 0;
    }

    public static Duration secondsToDurationOrNull(Long seconds) {
        return seconds != null ? Duration.ofSeconds(seconds) : null;
    }

    public static String durationToReadableStringOrNull(Duration duration) {
        if (duration == null) return null;
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        return String.format("%dh %dm %ds", hours, minutes, seconds);
    }
}
