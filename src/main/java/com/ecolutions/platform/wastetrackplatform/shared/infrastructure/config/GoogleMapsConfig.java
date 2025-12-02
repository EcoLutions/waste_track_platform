package com.ecolutions.platform.wastetrackplatform.shared.infrastructure.config;

import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class GoogleMapsConfig {
    @Value("${google.maps.api-key}")
    private String apiKey;

    @Value("${google.maps.request-timeout-seconds:30}")
    private long requestTimeoutSeconds;

    @Value("${google.maps.retry-timeout-seconds:60}")
    private long retryTimeoutSeconds;

    @Value("${google.maps.max-retries:3}")
    private int maxRetries;

    @Bean
    public GeoApiContext geoApiContext() {
        return new GeoApiContext.Builder()
                .apiKey(apiKey)
                .connectTimeout(requestTimeoutSeconds, TimeUnit.SECONDS)
                .readTimeout(requestTimeoutSeconds, TimeUnit.SECONDS)
                .writeTimeout(requestTimeoutSeconds, TimeUnit.SECONDS)
                .retryTimeout(retryTimeoutSeconds, TimeUnit.SECONDS)
                .maxRetries(maxRetries)
                .build();
    }
}
