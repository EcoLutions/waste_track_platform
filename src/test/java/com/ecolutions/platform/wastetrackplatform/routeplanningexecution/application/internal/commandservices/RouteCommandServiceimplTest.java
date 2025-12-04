package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.acl.contexts.MunicipalOperationsContextFacade;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.acl.dtos.DistrictConfigDTO;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.optimization.RouteOptimizationService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.update.RouteUpdateService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.websocket.RouteWebSocketPublisherService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates.Route;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.CreateRouteCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.DeleteRouteCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.UpdateRouteCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.infrastructure.persistence.jpa.repositories.RouteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RouteCommandServiceImplTest {

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private MunicipalOperationsContextFacade municipalOperationsContextFacade;

    @Mock
    private RouteUpdateService routeUpdateService;

    @Mock
    private RouteOptimizationService routeOptimizationService;

    @Mock
    private RouteWebSocketPublisherService routeWebSocketPublisher;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private RouteCommandServiceImpl routeCommandService;

    private CreateRouteCommand createCommand;
    private UpdateRouteCommand updateCommand;
    private DeleteRouteCommand deleteCommand;

    private Route mockRoute;

    @BeforeEach
    void setUp() {
        createCommand = new CreateRouteCommand(
                "DIST-001",
                "DRIVER-001",
                "VEHICLE-001",
                LocalDateTime.now().plusDays(1)
        );

        updateCommand = new UpdateRouteCommand(
                "1",
                LocalDateTime.now().plusDays(2)
        );

        deleteCommand = new DeleteRouteCommand("1");

        mockRoute = new Route(createCommand);
        mockRoute.setId("1");
    }


    @Test
    @DisplayName("Should create and return new route successfully")
    void shouldCreateRouteSuccessfully() {
        DistrictConfigDTO districtConfig = new DistrictConfigDTO(
                "DIST-001",
                "District 1",
                Duration.ofHours(8),
                LocalTime.of(6, 0),
                LocalTime.of(18, 0),
                java.math.BigDecimal.valueOf(-12.0464),
                java.math.BigDecimal.valueOf(-77.0428),
                java.math.BigDecimal.valueOf(-12.0500),
                java.math.BigDecimal.valueOf(-77.0500)
        );

        when(municipalOperationsContextFacade.getDistrictConfiguration("DIST-001"))
                .thenReturn(Optional.of(districtConfig));
        when(municipalOperationsContextFacade.validateScheduledTime(anyString(), any(LocalDateTime.class)))
                .thenReturn(true);
        when(routeRepository.findOverlappingRoutesForDriver(anyString(), any(LocalDateTime.class), any(LocalDateTime.class), anyList()))
                .thenReturn(Collections.emptyList());
        when(routeRepository.findOverlappingRoutesForVehicle(anyString(), any(LocalDateTime.class), any(LocalDateTime.class), anyList()))
                .thenReturn(Collections.emptyList());
        when(routeRepository.save(any(Route.class))).thenReturn(mockRoute);

        var result = routeCommandService.handle(createCommand);

        assertTrue(result.isPresent());
        assertEquals("DIST-001", result.get().getDistrictId().value());
        verify(routeRepository, times(1)).save(any(Route.class));
    }


    @Test
    @DisplayName("Should update existing route successfully")
    void shouldUpdateRouteSuccessfully() {
        when(routeRepository.findById("1")).thenReturn(Optional.of(mockRoute));
        when(routeRepository.save(any(Route.class))).thenReturn(mockRoute);

        var result = routeCommandService.handle(updateCommand);

        assertTrue(result.isPresent());
        verify(routeRepository).findById("1");
        verify(routeRepository).save(any(Route.class));
    }

    @Test
    @DisplayName("Should throw exception when route not found for update")
    void shouldThrowWhenUpdatingNonExistentRoute() {
        when(routeRepository.findById("1")).thenReturn(Optional.empty());

        var ex = assertThrows(IllegalArgumentException.class, () ->
                routeCommandService.handle(updateCommand));

        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    @DisplayName("Should delete existing route successfully")
    void shouldDeleteRouteSuccessfully() {
        when(routeRepository.findById("1")).thenReturn(Optional.of(mockRoute));
        doNothing().when(routeRepository).delete(mockRoute);

        var result = routeCommandService.handle(deleteCommand);

        assertTrue(result);
        verify(routeRepository).delete(mockRoute);
    }

    @Test
    @DisplayName("Should throw exception when route not found for deletion")
    void shouldThrowWhenDeletingNonExistentRoute() {
        when(routeRepository.findById("1")).thenReturn(Optional.empty());

        var ex = assertThrows(IllegalArgumentException.class, () ->
                routeCommandService.handle(deleteCommand));

        assertTrue(ex.getMessage().contains("not found"));
    }
}
