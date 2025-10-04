package com.ecolutions.platform.wastetrackplatform.shared.domain.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public final class StringUtils {
    private StringUtils() {}

    public static Set<String> parseCommaSeparatedValues(String expand) {
        if (expand == null || expand.trim().isEmpty()) {
            return Collections.emptySet();
        }
        return Arrays.stream(expand.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }
}
