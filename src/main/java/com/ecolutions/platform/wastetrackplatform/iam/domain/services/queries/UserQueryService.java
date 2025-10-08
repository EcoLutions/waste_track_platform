package com.ecolutions.platform.wastetrackplatform.iam.domain.services.queries;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.aggregates.User;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.queries.GetAllUsersQuery;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.queries.GetUserByIdQuery;

import java.util.List;
import java.util.Optional;

public interface UserQueryService {
    Optional<User> handle(GetUserByIdQuery query);
    List<User> handle(GetAllUsersQuery query);
}