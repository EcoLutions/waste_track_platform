package com.ecolutions.platform.wastetrackplatform.config;

import com.ecolutions.platform.wastetrackplatform.shared.infrastructure.storage.configuration.FirebaseConfig;
import com.ecolutions.platform.wastetrackplatform.shared.infrastructure.storage.services.FirebaseStorageService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("test")
public class MockFirebaseConfig {
    @Bean
    public FirebaseConfig firebaseConfig() {
        // Retorna un mock o una implementación vacía
        return Mockito.mock(FirebaseConfig.class);
    }

    @Bean
    public FirebaseStorageService firebaseStorageService() {
        return Mockito.mock(FirebaseStorageService.class);
    }
}
