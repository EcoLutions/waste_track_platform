package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.commandservices;

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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RouteCommandServiceImplTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private RouteCommandServiceImpl routeCommandService;

    private CreateRouteCommand createCommand;
    private UpdateRouteCommand updateCommand;
    private DeleteRouteCommand deleteCommand;

    private Route mockRoute;

    @BeforeEach
    void setUp() {
/*
        createCommand = new CreateRouteCommand("DIST-001", "REGULAR", LocalDate.now().plusDays(1));
*/
/*
        updateCommand = new UpdateRouteCommand("1", "DIST-002", "OPTIMIZED", LocalDate.now().plusDays(2));
*/
        deleteCommand = new DeleteRouteCommand("1");

        mockRoute.setId("1");
    }


    @Test
    @DisplayName("Should create and return new route successfully")
    void shouldCreateRouteSuccessfully() {
        when(routeRepository.save(any(Route.class))).thenReturn(mockRoute);

        var result = routeCommandService.handle(createCommand);

        assertTrue(result.isPresent());
        assertEquals("DIST-001", result.get().getDistrictId().value());
        verify(routeRepository, times(1)).save(any(Route.class));
    }

    @Test
    @DisplayName("Should throw exception when create route fails")
    void shouldThrowExceptionOnCreateFailure() {
        when(routeRepository.save(any(Route.class))).thenThrow(new RuntimeException("DB error"));

        var ex = assertThrows(IllegalArgumentException.class, () ->
                routeCommandService.handle(createCommand));

        assertTrue(ex.getMessage().contains("Failed to create route"));
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
