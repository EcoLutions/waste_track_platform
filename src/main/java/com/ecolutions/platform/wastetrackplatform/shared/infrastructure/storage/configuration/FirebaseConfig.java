package com.ecolutions.platform.wastetrackplatform.shared.infrastructure.storage.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(FirebaseProperties.class)
@Profile("!test")
public class FirebaseConfig {
    private final FirebaseProperties firebaseProperties;

    @PostConstruct
    public void initializeFirebase() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                validateFirebaseConfig();
                InputStream serviceAccount = getCredentialsInputStream();
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setStorageBucket(firebaseProperties.getStorage().getBucket())
                        .build();
                FirebaseApp.initializeApp(options);
                log.info("Firebase application initialized successfully with bucket: {}", firebaseProperties.getStorage().getBucket());
            } else {
                log.info("Firebase application already initialized");
            }
        } catch (IOException e) {
            log.error("Firebase initialization failed", e);
            throw new RuntimeException("Firebase initialization failed: " + e.getMessage(), e);
        }
    }

    @Bean
    public Storage storage() throws IOException {
        validateFirebaseConfig();
        InputStream serviceAccount = getCredentialsInputStream();
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        String projectId = extractProjectId();
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .build()
                .getService();

        log.info("Storage bean created successfully with project: {}", projectId);
        return storage;
    }

    @Bean
    public StorageClient storageClient() {
        try {
            StorageClient client = StorageClient.getInstance();
            log.info("StorageClient bean created successfully");
            return client;
        } catch (Exception e) {
            log.error("Failed to create StorageClient bean", e);
            throw new RuntimeException("Failed to initialize Firebase StorageClient", e);
        }
    }

    private void validateFirebaseConfig() {
        String firebaseConfig = firebaseProperties.getConfig();

        if (firebaseConfig == null || firebaseConfig.trim().isEmpty())
            throw new IllegalStateException("Firebase configuration is missing. Please set FIREBASE_CONFIG environment variable.");

        if (!firebaseConfig.trim().startsWith("{"))
            throw new IllegalStateException("Firebase configuration must be a valid JSON string");

        log.debug("Firebase config validated, length: {} chars", firebaseConfig.length());
    }

    private InputStream getCredentialsInputStream() {
        String firebaseConfig = firebaseProperties.getConfig();
        return new ByteArrayInputStream(firebaseConfig.getBytes(StandardCharsets.UTF_8));
    }

    private String extractProjectId() {
        try {
            String firebaseConfig = firebaseProperties.getConfig();
            JsonObject json = JsonParser.parseString(firebaseConfig).getAsJsonObject();
            String projectId = json.get("project_id").getAsString();

            if (projectId == null || projectId.isEmpty())
                throw new IllegalStateException("project_id not found in Firebase configuration");

            log.debug("Extracted project_id: {}", projectId);
            return projectId;

        } catch (Exception e) {
            log.error("Failed to extract project_id from Firebase config", e);
            throw new IllegalStateException("Invalid Firebase configuration JSON", e);
        }
    }
}