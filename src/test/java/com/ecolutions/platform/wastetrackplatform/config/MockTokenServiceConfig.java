// Java
package com.ecolutions.platform.wastetrackplatform.config;

import com.ecolutions.platform.wastetrackplatform.iam.application.internal.outboundservices.tokens.TokenService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("test")
public class MockTokenServiceConfig {
    @Bean
    @Primary
    public TokenService tokenService() {
        return Mockito.mock(TokenService.class);
    }
}