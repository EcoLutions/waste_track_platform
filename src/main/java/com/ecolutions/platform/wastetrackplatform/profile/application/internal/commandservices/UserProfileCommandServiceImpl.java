package com.ecolutions.platform.wastetrackplatform.profile.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.profile.domain.model.aggregates.UserProfile;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.commands.CreateUserProfileCommand;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.commands.DeleteUserProfileCommand;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.commands.InitializeUserProfileCommand;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.commands.UpdateUserProfileCommand;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.valueobjects.Photo;
import com.ecolutions.platform.wastetrackplatform.profile.domain.services.command.UserProfileCommandService;
import com.ecolutions.platform.wastetrackplatform.profile.infrastructure.persistence.jpa.repositories.UserProfileRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileCommandServiceImpl implements UserProfileCommandService {
    private final UserProfileRepository userProfileRepository;

    @Override
    public Optional<UserProfile> handle(CreateUserProfileCommand command) {
        var photo = Photo.of(command.photoUrl());
        UserProfile newUserProfile = new UserProfile(
            command.userId(),
            command.email(),
            command.phoneNumber(),
            command.language(),
            command.timezone(),
            photo,
            command.districtId()
        );

        try {
            var savedUserProfile = userProfileRepository.save(newUserProfile);
            return Optional.of(savedUserProfile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create user profile: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<UserProfile> handle(UpdateUserProfileCommand command) {
        UserProfile existingUserProfile = userProfileRepository.findById(command.userProfileId())
            .orElseThrow(() -> new IllegalArgumentException("UserProfile with ID " + command.userProfileId() + " not found."));

        if (command.photoUrl() != null) {
            existingUserProfile.setPhoto(new Photo(command.photoUrl()));
        }
        if (command.districtId() != null) {
            existingUserProfile.setDistrictId(command.districtId());
        }
        if (command.email() != null) {
            existingUserProfile.setEmail(command.email());
        }
        if (command.phoneNumber() != null) {
            existingUserProfile.setPhoneNumber(command.phoneNumber());
        }
        if (command.emailNotificationsEnabled() != null) {
            existingUserProfile.setEmailNotificationsEnabled(command.emailNotificationsEnabled());
        }
        if (command.smsNotificationsEnabled() != null) {
            existingUserProfile.setSmsNotificationsEnabled(command.smsNotificationsEnabled());
        }
        if (command.pushNotificationsEnabled() != null) {
            existingUserProfile.setPushNotificationsEnabled(command.pushNotificationsEnabled());
        }
        if (command.language() != null) {
            existingUserProfile.setLanguage(command.language());
        }
        if (command.timezone() != null) {
            existingUserProfile.setTimezone(command.timezone());
        }

        try {
            var updatedUserProfile = userProfileRepository.save(existingUserProfile);
            return Optional.of(updatedUserProfile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update user profile: " + e.getMessage(), e);
        }
    }

    @Override
    public Boolean handle(DeleteUserProfileCommand command) {
        UserProfile existingUserProfile = userProfileRepository.findById(command.userProfileId())
            .orElseThrow(() -> new IllegalArgumentException("UserProfile with ID " + command.userProfileId() + " not found."));
        try {
            userProfileRepository.delete(existingUserProfile);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to delete user profile: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<UserProfile> handle(InitializeUserProfileCommand command) {
        userProfileRepository.findByUserId(new UserId(command.userId()))
                .ifPresent(_ -> {throw new IllegalArgumentException("UserProfile for User ID " + command.userId() + " already exists.");});
        userProfileRepository.findByEmail(new EmailAddress(command.email()))
                .ifPresent(_ -> {throw new IllegalArgumentException("UserProfile with Email " + command.email() + " already exists.");});
        try {
            UserProfile newUserProfile = new UserProfile(command);
            return Optional.of(userProfileRepository.save(newUserProfile));
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve user profile: " + e.getMessage(), e);
        }
    }
}