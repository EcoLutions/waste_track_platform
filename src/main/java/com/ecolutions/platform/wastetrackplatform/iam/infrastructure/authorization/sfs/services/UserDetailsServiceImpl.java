package com.ecolutions.platform.wastetrackplatform.iam.infrastructure.authorization.sfs.services;

import com.ecolutions.platform.wastetrackplatform.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.ecolutions.platform.wastetrackplatform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service(value = "defaultUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(new EmailAddress(username))
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + username));
        return UserDetailsImpl.build(user);
    }
}
