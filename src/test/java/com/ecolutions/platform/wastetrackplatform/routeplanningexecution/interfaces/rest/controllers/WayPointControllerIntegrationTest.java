package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.config.MockFirebaseConfig;
import com.ecolutions.platform.wastetrackplatform.config.MockTokenServiceConfig;
import com.ecolutions.platform.wastetrackplatform.config.TestSecurityConfig;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.CreateWayPointCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.DeleteWayPointCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.UpdateWayPointCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.entities.WayPoint;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetAllWayPointsQuery;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetWayPointByIdQuery;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.*;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.services.command.WayPointCommandService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.services.queries.WayPointQueryService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request.CreateWayPointResource;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request.UpdateWayPointResource;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.response.WayPointResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.ContainerId;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
    @DisplayName("Should create a WayPoint successfully")
    void shouldCreateWayPointSuccessfully() {

        CreateWayPointResource resource = new CreateWayPointResource("CONTAINER-01", 1, "HIGH", LocalDateTime.now().plusMinutes(10), "Nota del conductor");

        WayPoint waypoint = new WayPoint(ContainerId.of("CONTAINER-01"), 1, new Priority(PriorityLevel.HIGH));
        waypoint.setId("WP-1");

        when(wayPointCommandService.handle((CreateWayPointCommand) any())).thenReturn(Optional.of(waypoint));

        ResponseEntity<WayPointResource> response =
                restTemplate.postForEntity(baseUrl + "?routeId=ROUTE-001", resource, WayPointResource.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("Should get WayPoint by ID successfully")
    void shouldGetWayPointByIdSuccessfully() {
        WayPoint waypoint = new WayPoint(ContainerId.of("C1"), 1, new Priority(PriorityLevel.MEDIUM));
        waypoint.setId("WP-123");
        waypoint.setActualArrivalTime(LocalDateTime.now());
        waypoint.setServiceTime(Duration.ofMinutes(5));

        when(wayPointQueryService.handle((GetWayPointByIdQuery) any())).thenReturn(Optional.of(waypoint));

        ResponseEntity<WayPointResource> response =
                restTemplate.getForEntity(baseUrl + "/WP-123", WayPointResource.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Should get all WayPoints successfully")
    void shouldGetAllWayPointsSuccessfully() {
        WayPoint wp1 = new WayPoint(ContainerId.of("C1"), 1, new Priority(PriorityLevel.HIGH));
        WayPoint wp2 = new WayPoint(ContainerId.of("C2"), 2, new Priority(PriorityLevel.LOW));
        when(wayPointQueryService.handle((GetAllWayPointsQuery) any())).thenReturn(List.of(wp1, wp2));

        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Should update a WayPoint successfully")
    void shouldUpdateWayPointSuccessfully() {
        UpdateWayPointResource resource = new UpdateWayPointResource(1, "HIGH", LocalDateTime.now().plusMinutes(10), "Nota del conductor");

        WayPoint updated = new WayPoint(ContainerId.of("C1"), 1, new Priority(PriorityLevel.HIGH));
        when(wayPointCommandService.handle((UpdateWayPointCommand) any())).thenReturn(Optional.of(updated));

        restTemplate.put(URI.create(baseUrl + "/WP-123"), resource);

        Mockito.verify(wayPointCommandService).handle((UpdateWayPointCommand) any());
    }

    @Test
    @DisplayName("Should delete WayPoint successfully")
    void shouldDeleteWayPointSuccessfully() {
        when(wayPointCommandService.handle((DeleteWayPointCommand) any())).thenReturn(true);

        restTemplate.delete(URI.create(baseUrl + "/WP-123"));

        Mockito.verify(wayPointCommandService).handle((DeleteWayPointCommand) any());
    }
}
