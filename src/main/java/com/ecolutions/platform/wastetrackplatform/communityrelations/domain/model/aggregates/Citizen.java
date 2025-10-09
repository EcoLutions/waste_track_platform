package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.valueobjects.MembershipLevel;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.valueobjects.RewardPoints;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Citizen extends AuditableAbstractAggregateRoot<Citizen> {

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "user_id", nullable = false, unique = true))
    private UserId userId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "district_id", nullable = false))
    private DistrictId districtId;

    @Embedded
    @NotNull
    private FullName fullName;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email", nullable = false, unique = true))
    private EmailAddress email;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "phone_number", nullable = false, unique = true))
    private PhoneNumber phoneNumber;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "total_points"))
    private RewardPoints totalPoints;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MembershipLevel membershipLevel;

    private Integer totalReportsSubmitted;

    @NotNull
    private LocalDateTime lastActivityDate;

    public Citizen() {
        super();
        this.totalPoints = RewardPoints.of(0);
        this.membershipLevel = MembershipLevel.BRONZE;
        this.totalReportsSubmitted = 0;
        this.lastActivityDate = LocalDateTime.now();
    }

    public Citizen(UserId userId, DistrictId districtId, FullName fullName,
                   EmailAddress email, PhoneNumber phoneNumber) {
        this();
        this.userId = userId;
        this.districtId = districtId;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public void earnPoints(Integer points, String reason) {
        if (points == null || points <= 0) {
            throw new IllegalArgumentException("Points to earn must be positive");
        }
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Reason cannot be null or blank");
        }

        this.totalPoints = this.totalPoints.add(points);
        this.lastActivityDate = LocalDateTime.now();
        updateMembershipLevel();
    }

    public void redeemPoints(Integer points) {
        if (points == null || points <= 0) {
            throw new IllegalArgumentException("Points to redeem must be positive");
        }
        if (!this.totalPoints.canSubtract(points)) {
            throw new IllegalArgumentException("Insufficient points to redeem");
        }

        this.totalPoints = this.totalPoints.subtract(points);
        this.lastActivityDate = LocalDateTime.now();
    }

    public void submitReport() {
        this.totalReportsSubmitted++;
        this.lastActivityDate = LocalDateTime.now();
        updateMembershipLevel();
    }

    public void updateMembershipLevel() {
        MembershipLevel newLevel = calculateMembershipLevel();
        if (this.membershipLevel != newLevel) {
            MembershipLevel oldLevel = this.membershipLevel;
            this.membershipLevel = newLevel;
        }
    }

    private MembershipLevel calculateMembershipLevel() {
        int points = this.totalPoints.value();
        int reports = this.totalReportsSubmitted;

        if (points >= 500 || reports >= 50) {
            return MembershipLevel.GOLD;
        } else if (points >= 100 || reports >= 10) {
            return MembershipLevel.SILVER;
        } else {
            return MembershipLevel.BRONZE;
        }
    }

    public boolean isActive() {
        return this.lastActivityDate.isAfter(LocalDateTime.now().minusMonths(6));
    }

    public void updateContactInfo(FullName fullName, EmailAddress email, PhoneNumber phoneNumber) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.lastActivityDate = LocalDateTime.now();
    }

    public void changeDistrict(DistrictId districtId) {
        this.districtId = districtId;
        this.lastActivityDate = LocalDateTime.now();

    }
}
