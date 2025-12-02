package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.config.MockFirebaseConfig;
import com.ecolutions.platform.wastetrackplatform.config.MockTokenServiceConfig;
import com.ecolutions.platform.wastetrackplatform.config.TestSecurityConfig;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.DeleteWayPointCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.services.command.WayPointCommandService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.services.queries.WayPointQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import({MockFirebaseConfig.class, TestSecurityConfig.class, MockTokenServiceConfig.class})
class WayPointControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private WayPointCommandService wayPointCommandService;

    @MockBean
    private WayPointQueryService wayPointQueryService;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1/waypoints";
    }

    @Test
    @DisplayName("Should delete WayPoint successfully")
    void shouldDeleteWayPointSuccessfully() {
        when(wayPointCommandService.handle((DeleteWayPointCommand) any())).thenReturn(true);

        restTemplate.delete(URI.create(baseUrl + "/WP-123"));

        Mockito.verify(wayPointCommandService).handle((DeleteWayPointCommand) any());
    }
}
