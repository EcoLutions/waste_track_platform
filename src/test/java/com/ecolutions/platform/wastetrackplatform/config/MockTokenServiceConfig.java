// Java
package com.ecolutions.platform.wastetrackplatform.config;

import com.ecolutions.platform.wastetrackplatform.iam.application.internal.outboundservices.tokens.TokenService;
import com.ecolutions.platform.wastetrackplatform.iam.infrastructure.tokens.jwt.services.TokenServiceImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class MockTokenServiceConfig {
    @Bean
    @Primary
    public TokenService tokenService() {
        return new TokenService() {
            @Override
            public String generateToken(String username) {
                return "dummy-token";
            }

            @Override
            public String getUsernameFromToken(String token) {
                return "";
            }

            @Override
            public boolean validateToken(String token) {
                return false;
            }
            // Implementa los métodos requeridos con lógica dummy
        };
    }
}