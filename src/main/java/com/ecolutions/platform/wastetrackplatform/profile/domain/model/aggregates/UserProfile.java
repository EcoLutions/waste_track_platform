package com.ecolutions.platform.wastetrackplatform.profile.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.profile.domain.model.commands.InitializeUserProfileCommand;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.valueobjects.Language;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.valueobjects.Photo;
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

    public UserProfile() {
        super();
        this.deviceTokens = new ArrayList<>();
        this.emailNotificationsEnabled = true;
        this.smsNotificationsEnabled = false;
        this.pushNotificationsEnabled = true;
        this.language = Language.ES;
        this.timezone = "America/Lima";
    }

    public UserProfile(UserId userId, EmailAddress email, PhoneNumber phoneNumber, Language language, String timezone, Photo photo, DistrictId districtId) {
        this();
        this.userId = userId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.language = language;
        this.timezone = timezone;
        this.photo = photo;
        this.districtId = districtId;
    }

    public UserProfile(InitializeUserProfileCommand command) {
        this();
        this.email = new EmailAddress(command.email());
        this.userId = new UserId(command.userId());
        this.districtId = new DistrictId(command.districtId());
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
}
