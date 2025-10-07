package com.ecolutions.platform.wastetrackplatform.profile.domain.services.queries;

import com.ecolutions.platform.wastetrackplatform.profile.domain.model.aggregates.UserProfile;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.queries.GetAllUserProfilesQuery;
import com.ecolutions.platform.wastetrackplatform.profile.domain.model.queries.GetUserProfileByIdQuery;

import java.util.List;
import java.util.Optional;

public interface UserProfileQueryService {
    Optional<UserProfile> handle(GetUserProfileByIdQuery query);
    List<UserProfile> handle(GetAllUserProfilesQuery query);
}