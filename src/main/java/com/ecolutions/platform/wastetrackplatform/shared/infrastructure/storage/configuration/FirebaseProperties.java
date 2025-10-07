package com.ecolutions.platform.wastetrackplatform.shared.infrastructure.storage.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "firebase")
@Component
@Data
public class FirebaseProperties {
    private String config;
    private Storage storage = new Storage();

    @Data
    public static class Storage {
        private String bucket;
        private String basePath = "evidences";
        private Integer urlExpirationHours = 1;
    }
}