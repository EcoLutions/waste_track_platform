package com.ecolutions.platform.wastetrackplatform.profile.application.internal.queryservices;

import com.ecolutions.platform.wastetrackplatform.profile.domain.model.aggregates.UserProfile;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.queries.GetAllUserProfilesQuery;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.queries.GetUserProfileByIdQuery;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.queries.GetUserProfileByUserIdQuery;
import com.ecolutions.platform.wastetrackplatform.profile.domain.services.queries.UserProfileQueryService;
import com.ecolutions.platform.wastetrackplatform.profile.infrastructure.persistence.jpa.repositories.UserProfileRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileQueryServiceImpl implements UserProfileQueryService {
    private final UserProfileRepository userProfileRepository;

    @Override
    public Optional<UserProfile> handle(GetUserProfileByIdQuery query) {
        try {
            return userProfileRepository.findById(query.userProfileId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve user profile: " + e.getMessage(), e);
        }
    }

    @Override
    public List<UserProfile> handle(GetAllUserProfilesQuery query) {
        try {
            return userProfileRepository.findAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve user profiles: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<UserProfile> handle(GetUserProfileByUserIdQuery userId) {
        try {
            return userProfileRepository.findByUserId(new UserId(userId.userId()));
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve user profile by user ID: " + e.getMessage(), e);
        }
    }
}