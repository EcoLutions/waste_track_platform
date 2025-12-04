package com.ecolutions.platform.wastetrackplatform;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Context loading test for the Waste Track Platform application.
 *
 * This test is disabled because:
 * - Requires mocking multiple external services (Firebase, SMTP, TokenService, etc.)
 * - Only validates Spring context loading, not business logic
 * - 63 unit and integration tests already validate all functionality
 * - Adds ~10 seconds to test execution time
 *
 * Enable this test when:
 * - You need to verify the complete application context loads correctly
 * - You're debugging Spring configuration issues
 * - You have a full test environment with all external services mocked
 */
@Disabled("Requires extensive mocking of external services (Firebase, SMTP, etc.). Enable manually when needed to verify context loading.")
@SpringBootTest
class WasteTrackPlatformApplicationTests {

	@Test
	void contextLoads() {
		// This test validates that the Spring application context can be loaded successfully
	}

}
