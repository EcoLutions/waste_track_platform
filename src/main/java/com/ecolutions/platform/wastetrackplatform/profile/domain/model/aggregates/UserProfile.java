package com.ecolutions.platform.wastetrackplatform.profile.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.profile.domain.model.valueobjects.Language;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.valueobjects.Photo;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.valueobjects.UserType;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.PhoneNumber;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.UserId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class UserProfile extends AuditableAbstractAggregateRoot<UserProfile> {
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "user_id", nullable = false))
    private UserId userId;

    @Embedded
    private Photo photo;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "district_id"))
    private DistrictId districtId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email", nullable = false))
    private EmailAddress email;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "phone_number"))
    private PhoneNumber phoneNumber;

    @NotNull
    private Boolean emailNotificationsEnabled;

    @NotNull
    private Boolean smsNotificationsEnabled;

    @NotNull
    private Boolean pushNotificationsEnabled;

    @ElementCollection
    @CollectionTable(name = "user_device_tokens", joinColumns = @JoinColumn(name = "user_profile_id"))
    @Column(name = "device_token")
    private List<String> deviceTokens;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Language language;

    @NotNull
    private String timezone;

    @NotNull
    private Boolean isActive;

    public UserProfile() {
        super();
        this.deviceTokens = new ArrayList<>();
        this.emailNotificationsEnabled = true;
        this.smsNotificationsEnabled = false;
        this.pushNotificationsEnabled = true;
        this.language = Language.ES;
        this.timezone = "America/Lima";
        this.isActive = true;
    }

    public UserProfile(UserId userId, UserType userType, EmailAddress email) {
        this();
        this.userId = userId;
        this.userType = userType;
        this.email = email;
    }

    public void enableNotificationChannel() {
        this.emailNotificationsEnabled = true;
    }

    public void disableNotificationChannel() {
        this.emailNotificationsEnabled = false;
    }

    public void addDeviceToken(String token) {
        if (token != null && !token.isBlank() && !this.deviceTokens.contains(token)) {
            this.deviceTokens.add(token);
        }
    }

    public void removeDeviceToken(String token) {
        this.deviceTokens.remove(token);
    }

    public boolean isNotificationEnabledFor() {
        return this.emailNotificationsEnabled || this.smsNotificationsEnabled || this.pushNotificationsEnabled;
    }

    public void deactivate() {
        this.isActive = false;
    }
}
