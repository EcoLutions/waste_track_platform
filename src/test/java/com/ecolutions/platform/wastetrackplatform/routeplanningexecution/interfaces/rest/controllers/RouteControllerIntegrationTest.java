package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.config.MockFirebaseConfig;
import com.ecolutions.platform.wastetrackplatform.config.MockTokenServiceConfig;
import com.ecolutions.platform.wastetrackplatform.config.TestSecurityConfig;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request.CreateRouteResource;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request.UpdateRouteResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Import({MockFirebaseConfig.class, TestSecurityConfig.class, MockTokenServiceConfig.class})
class RouteControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1/routes";
    }


    @Test
    @DisplayName("POST /api/v1/routes should create a new route successfully")
    void shouldCreateRouteSuccessfully() {
        CreateRouteResource request = new CreateRouteResource(
                "DIST-001",
                "REGULAR",
                LocalDate.now().plusDays(1).toString()
        );

        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl, request, Map.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("districtId"));
        assertEquals("DIST-001", response.getBody().get("districtId"));
    }


    @Test
    @DisplayName("GET /api/v1/routes should return list of routes")
    void shouldReturnListOfRoutes() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    @DisplayName("PUT /api/v1/routes/{id} should update route successfully if exists")
    void shouldUpdateRouteSuccessfully() {
        CreateRouteResource createRequest = new CreateRouteResource(
                "DIST-002",
                "EMERGENCY",
                LocalDate.now().plusDays(2).toString()
        );
        ResponseEntity<Map> created = restTemplate.postForEntity(baseUrl, createRequest, Map.class);
        assertEquals(HttpStatus.CREATED, created.getStatusCode());

        String routeId = created.getBody().get("id").toString();

        UpdateRouteResource updateRequest = new UpdateRouteResource(
                routeId,
                "DIST-002",
                "REGULAR",
                LocalDate.now().plusDays(3)
        );

        HttpEntity<UpdateRouteResource> entity = new HttpEntity<>(updateRequest);
        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl + "/" + routeId,
                HttpMethod.PUT,
                entity,
                Map.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().get("routeType").toString().contains("REGULAR"));
    }


    @Test
    @DisplayName("GET /api/v1/routes/{id} should return specific route")
    void shouldGetRouteById() {
        CreateRouteResource createRequest = new CreateRouteResource(
                "DIST-003",
                "REGULAR",
                LocalDate.now().plusDays(2).toString()
        );

        ResponseEntity<Map> created = restTemplate.postForEntity(baseUrl, createRequest, Map.class);
        assertEquals(HttpStatus.CREATED, created.getStatusCode());
        String routeId = created.getBody().get("id").toString();

        ResponseEntity<Map> response = restTemplate.getForEntity(baseUrl + "/" + routeId, Map.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("DIST-003", response.getBody().get("districtId"));
    }


    @Test
    @DisplayName("DELETE /api/v1/routes/{id} should delete route successfully")
    void shouldDeleteRouteSuccessfully() {
        CreateRouteResource createRequest = new CreateRouteResource(
                "DIST-004",
                "REGULAR",
                LocalDate.now().plusDays(5).toString()
        );
        ResponseEntity<Map> created = restTemplate.postForEntity(baseUrl, createRequest, Map.class);
        String routeId = created.getBody().get("id").toString();

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + routeId,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}
