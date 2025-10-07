package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.valueobjects;

public record RewardPoints(Integer value) {
    public RewardPoints {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("Reward points value cannot be null or negative");
        }
    }

    public static RewardPoints of(Integer value) {
        if (value == null || value < 0) {
            return null;
        }
        return new RewardPoints(value);
    }

    public static String toStringOrNull(RewardPoints rewardPoints) {
        return rewardPoints != null ? rewardPoints.value().toString() : null;
    }

    public RewardPoints add(Integer points) {
        if (points == null || points < 0) {
            throw new IllegalArgumentException("Points to add cannot be null or negative");
        }
        return new RewardPoints(this.value + points);
    }

    public RewardPoints subtract(Integer points) {
        if (points == null || points < 0) {
            throw new IllegalArgumentException("Points to subtract cannot be null or negative");
        }
        if (this.value < points) {
            throw new IllegalArgumentException("Cannot subtract more points than available");
        }
        return new RewardPoints(this.value - points);
    }

    public boolean canSubtract(Integer points) {
        return points != null && this.value >= points;
    }
}