package com.ecolutions.platform.wastetrackplatform.communityrelations.infrastructure.storage.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class FirebaseConfig {
    private final FirebaseProperties firebaseProperties;

    @PostConstruct
    public void initializeFirebase() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                InputStream serviceAccount = getInputStream();
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setStorageBucket(firebaseProperties.getStorage().getBucket())
                        .build();

                FirebaseApp.initializeApp(options);
                log.info("✅ Firebase application initialized successfully with bucket: {}", firebaseProperties.getStorage().getBucket());
            } else {
                log.info("Firebase application already initialized");
            }

        } catch (IOException e) {
            log.error("❌ Firebase initialization failed", e);
            throw new RuntimeException("Firebase initialization failed: " + e.getMessage(), e);
        }
    }

    private InputStream getInputStream() {
        String firebaseConfig = firebaseProperties.getConfig();
        if (firebaseConfig == null || firebaseConfig.trim().isEmpty())
            throw new IllegalStateException("Firebase configuration is missing. Please set FIREBASE_CONFIG environment variable.");
        return new ByteArrayInputStream(firebaseConfig.getBytes(StandardCharsets.UTF_8));
    }

    @Bean
    public Storage storage() {
        try {
            return StorageOptions.getDefaultInstance().getService();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Google Cloud Storage", e);
        }
    }

    @Bean
    public StorageClient storageClient() {
        try {
            return StorageClient.getInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Firebase StorageClient", e);
        }
    }
}