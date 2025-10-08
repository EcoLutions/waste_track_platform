package com.ecolutions.platform.wastetrackplatform.iam.infrastructure.tokens.jwt;


import com.ecolutions.platform.wastetrackplatform.iam.application.internal.outboundservices.tokens.TokenService;
import org.springframework.security.core.Authentication;
import jakarta.servlet.http.HttpServletRequest;

public interface BearerTokenService extends TokenService {
    String getBearerTokenFrom(HttpServletRequest token);
    String generateToken(Authentication authentication);
}
