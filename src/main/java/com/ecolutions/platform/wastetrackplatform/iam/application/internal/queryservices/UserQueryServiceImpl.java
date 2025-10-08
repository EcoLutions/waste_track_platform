package com.ecolutions.platform.wastetrackplatform.iam.application.internal.queryservices;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.aggregates.User;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.queries.GetAllUsersQuery;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.queries.GetUserByIdQuery;
import com.ecolutions.platform.wastetrackplatform.iam.domain.services.queries.UserQueryService;
import com.ecolutions.platform.wastetrackplatform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;

    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        try {
            return userRepository.findById(query.userId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve user: " + e.getMessage(), e);
        }
    }

    @Override
    public List<User> handle(GetAllUsersQuery query) {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve users: " + e.getMessage(), e);
        }
    }
}