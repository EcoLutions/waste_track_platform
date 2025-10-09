package com.ecolutions.platform.wastetrackplatform.profile.infrastructure.persistence.jpa.repositories;

import com.ecolutions.platform.wastetrackplatform.profile.domain.model.aggregates.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
}
