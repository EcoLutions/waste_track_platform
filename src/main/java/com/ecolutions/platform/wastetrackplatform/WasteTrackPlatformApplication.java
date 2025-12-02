package com.ecolutions.platform.wastetrackplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@SpringBootApplication
@ConfigurationPropertiesScan
@EnableScheduling
public class WasteTrackPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(WasteTrackPlatformApplication.class, args);
	}

}
