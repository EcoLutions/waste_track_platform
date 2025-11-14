package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.RouteStatus;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.RouteType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


class RouteTest {

    private Route route;

    @BeforeEach
    void setUp() {
/*
        route = new Route(DistrictId.of("DIST-001"), RouteType.REGULAR, LocalDate.now().plusDays(1));
*/
    }


    @Test
    @DisplayName("Should create route with default DRAFT status")
    void shouldCreateRouteWithDefaultDraftStatus() {
        assertEquals(RouteStatus.ASSIGNED, route.getStatus());
        assertEquals(RouteType.REGULAR, route.getRouteType());
        assertTrue(route.getWaypoints().isEmpty());
    }



    @Test
    @DisplayName("Should start execution when route is ASSIGNED")
    void shouldStartExecutionSuccessfully() {
        route.setStatus(RouteStatus.ASSIGNED);
        route.startExecution();

        assertEquals(RouteStatus.IN_PROGRESS, route.getStatus());
        assertNotNull(route.getStartedAt());
    }

    @Test
    @DisplayName("Should throw when starting if not ASSIGNED")
    void shouldThrowWhenStartingInvalidStatus() {
        route.setStatus(RouteStatus.ASSIGNED);
        assertThrows(IllegalStateException.class, route::startExecution);
    }


    @Test
    @DisplayName("Should complete route execution when IN_PROGRESS")
    void shouldCompleteExecutionSuccessfully() {
        route.setStatus(RouteStatus.IN_PROGRESS);
        route.setStartedAt(LocalDateTime.now().minusMinutes(10));

        route.completeExecution();

        assertEquals(RouteStatus.COMPLETED, route.getStatus());
        assertNotNull(route.getCompletedAt());
        assertTrue(route.getActualDuration().toMinutes() >= 0);
    }

    @Test
    @DisplayName("Should throw when completing if not IN_PROGRESS")
    void shouldThrowWhenCompletingInvalidStatus() {
        route.setStatus(RouteStatus.ASSIGNED);
        assertThrows(IllegalStateException.class, route::completeExecution);
    }

/*

    @Test
    @DisplayName("Should add waypoint when route is modifiable (DRAFT or ASSIGNED)")
    void shouldAddWaypointWhenModifiable() {
        route.setStatus(RouteStatus.DRAFT);
        route.addWayPoint(new WayPoint(ContainerId.of("C1"), 1, new Priority(PriorityLevel.MEDIUM)));

        assertEquals(1, route.getWaypoints().size());
    }

    @Test
    @DisplayName("Should not allow modifying route when IN_PROGRESS")
    void shouldThrowWhenModifyingInProgress() {
        route.setStatus(RouteStatus.IN_PROGRESS);
        assertTrue(route.canBeModified());
        assertThrows(IllegalStateException.class,
                () -> route.addWaypoint(ContainerId.of("C2"), new Priority(PriorityLevel.HIGH)));
    }

    @Test
    @DisplayName("Should remove waypoint and reorder sequence")
    void shouldRemoveWaypointAndReorder() {
        WayPoint wp1 = new WayPoint(ContainerId.of("C1"), 1, new Priority(PriorityLevel.LOW));
        wp1.setId("WP1");

        WayPoint wp2 = new WayPoint(ContainerId.of("C2"), 2, new Priority(PriorityLevel.MEDIUM));
        wp2.setId("WP2");

        route.addWayPoint(wp1);
        route.addWayPoint(wp2);

        route.setStatus(RouteStatus.DRAFT);
        route.removeWaypoint(wp1.getId());

        assertEquals(1, route.getWaypoints().size());
    }


    @Test
    @DisplayName("Should mark waypoint as visited and update arrival time, service time, and status")
    void shouldMarkWaypointAsVisited() {
        WayPoint wp = new WayPoint(ContainerId.of("C1"), 1, new Priority(PriorityLevel.MEDIUM));
        wp.setId("WP1");

        route.addWayPoint(wp);

        LocalDateTime now = LocalDateTime.now();

        route.markWaypointAsVisited(wp.getId(), now);

        assertNotNull(wp.getActualArrivalTime(), "Actual arrival time should be set");
        assertEquals(now, wp.getActualArrivalTime(), "Arrival time should match the timestamp provided");
        assertEquals(Duration.ofMinutes(5), wp.getServiceTime(), "Default service time should be 5 minutes");
        assertEquals(WaypointStatus.VISITED, wp.getStatus(), "Waypoint should be marked as VISITED");
        assertTrue(wp.isCompleted(), "Waypoint should be considered completed");
    }
*/



    @Test
    @DisplayName("Should throw when marking non-existent waypoint")
    void shouldThrowWhenWaypointNotFound() {
        assertThrows(IllegalArgumentException.class, () ->
                route.markWaypointAsVisited("FAKE-ID", LocalDateTime.now()));
    }


/*    @Test
    @DisplayName("Should detect overdue routes")
    void shouldDetectOverdueRoute() {
        route.setScheduledStartAt(LocalDate.now().minusDays(1));
        route.setStatus(RouteStatus.ASSIGNED);
        assertTrue(route.isOverdue());
    }*/

    @Test
    @DisplayName("Should not mark completed routes as overdue")
    void shouldNotBeOverdueWhenCompleted() {
        route.setStatus(RouteStatus.COMPLETED);
        assertFalse(route.isOverdue());
    }
}
