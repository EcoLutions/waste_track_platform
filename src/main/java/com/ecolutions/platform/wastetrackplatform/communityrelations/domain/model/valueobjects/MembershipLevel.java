package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.valueobjects;

public enum MembershipLevel {
    BRONZE,
    SILVER,
    GOLD;

    public static MembershipLevel fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Membership level cannot be null or blank");
        }
        try {
            return MembershipLevel.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid membership level: " + value);
        }
    }

    public MembershipLevel promote() {
        return switch (this) {
            case BRONZE -> SILVER;
            case SILVER, GOLD -> GOLD;
        };
    }

    public MembershipLevel demote() {
        return switch (this) {
            case BRONZE, SILVER -> BRONZE;
            case GOLD -> SILVER;
        };
    }

    public boolean isAtLeast(MembershipLevel other) {
        return this.ordinal() >= other.ordinal();
    }

    public int getPointsThreshold() {
        return switch (this) {
            case BRONZE -> 0;
            case SILVER -> 100;
            case GOLD -> 500;
        };
    }
}