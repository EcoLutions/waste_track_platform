package com.ecolutions.platform.wastetrackplatform.communityrelations.infrastructure.storage.configuration;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "firebase")
@Validated
@Data
public class FirebaseProperties {
    @NotBlank(message = "Firebase config is required")
    private String config;

    private Storage storage = new Storage();

    @Data
    public static class Storage {
        @NotBlank(message = "Firebase bucket name is required")
        private String bucket;
        private String basePath = "evidences";
        private Integer urlExpirationHours = 1;
    }
}