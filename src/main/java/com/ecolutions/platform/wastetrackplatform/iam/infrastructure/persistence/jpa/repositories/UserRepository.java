package com.ecolutions.platform.wastetrackplatform.iam.infrastructure.persistence.jpa.repositories;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.aggregates.User;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.valueobjects.Username;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Boolean existsByUsername(Username username);
    Optional<User> findByUsername(Username username);
    Boolean existsByEmail(EmailAddress email);
    Optional<User> findByEmail(EmailAddress email);
}