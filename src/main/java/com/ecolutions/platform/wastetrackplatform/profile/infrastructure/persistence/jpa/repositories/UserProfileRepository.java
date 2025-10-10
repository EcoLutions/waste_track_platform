package com.ecolutions.platform.wastetrackplatform.profile.infrastructure.persistence.jpa.repositories;

import com.ecolutions.platform.wastetrackplatform.profile.domain.model.aggregates.UserProfile;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
    Optional<UserProfile> findByUserId(UserId userId);
}
