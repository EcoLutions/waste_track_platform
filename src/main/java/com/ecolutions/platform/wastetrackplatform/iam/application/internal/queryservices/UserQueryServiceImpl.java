package com.ecolutions.platform.wastetrackplatform.iam.application.internal.queryservices;


import com.ecolutions.platform.wastetrackplatform.iam.domain.model.aggregates.User;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.queries.GetAllUsersQuery;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.queries.GetCurrentUserQuery;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.queries.GetUserByIdQuery;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.queries.GetUserByEmailQuery;
import com.ecolutions.platform.wastetrackplatform.iam.domain.services.UserQueryService;
import com.ecolutions.platform.wastetrackplatform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;

    @Override
    public List<User> handle(GetAllUsersQuery query) {
        return userRepository.findAll();
    }


    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        return userRepository.findById(query.userId());
    }

    @Override
    public Optional<User> handle(GetUserByEmailQuery query) {
        return userRepository.findByEmail(new EmailAddress(query.username()));
    }

    @Override
    public Optional<User> handle(GetCurrentUserQuery query) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String email = authentication.getName();
                return userRepository.findByEmail(new EmailAddress(email));
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve current user: " + e.getMessage(), e);
        }
    }
}
