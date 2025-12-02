package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.entities;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.PriorityLevel;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.WaypointStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


class WayPointTest {

    private WayPoint waypoint;

    @BeforeEach
    void setUp() {
/*        waypoint = new WayPoint(
                ContainerId.of("CONT-001"),
                1,
                new Priority(PriorityLevel.MEDIUM)
        );*/
    }


    @Test
    @DisplayName("Should create waypoint with default PENDING status")
    void shouldCreateWaypointWithPendingStatus() {
        assertEquals(WaypointStatus.PENDING, waypoint.getStatus());
        assertEquals(1, waypoint.getSequenceOrder());
        assertNotNull(waypoint.getPriority());
        assertEquals(PriorityLevel.MEDIUM, waypoint.getPriority().level());
    }


    @Test
    @DisplayName("Should mark waypoint as visited and update fields")
    void shouldMarkWaypointAsVisited() {
        LocalDateTime arrival = LocalDateTime.now();
        Duration serviceTime = Duration.ofMinutes(7);

        /* waypoint.markAsVisited(arrival, serviceTime);*/

        assertEquals(WaypointStatus.VISITED, waypoint.getStatus());
        assertEquals(arrival, waypoint.getActualArrivalTime());
        /*assertEquals(serviceTime, waypoint.getServiceTime());*/
        assertTrue(waypoint.isCompleted(), "Waypoint should be marked as completed after visiting");
    }

/*
    @Test
    @DisplayName("Should override previous visit data on re-marking")
    void shouldOverridePreviousVisitData() {
        LocalDateTime firstArrival = LocalDateTime.now().minusMinutes(10);
        waypoint.markAsVisited(firstArrival, Duration.ofMinutes(5));

        LocalDateTime newArrival = LocalDateTime.now();
        waypoint.markAsVisited(newArrival, Duration.ofMinutes(6));

        assertEquals(newArrival, waypoint.getActualArrivalTime());
        assertEquals(Duration.ofMinutes(6), waypoint.getServiceTime());
    }
*/


    @Test
    @DisplayName("Should mark waypoint as skipped and store reason")
    void shouldMarkWaypointAsSkipped() {
        /*waypoint.markAsSkipped("Container inaccessible due to blockage");

        assertEquals(WaypointStatus.SKIPPED, waypoint.getStatus());
        assertEquals("Container inaccessible due to blockage", waypoint.getDriverNote());*/
        assertFalse(waypoint.isCompleted());
    }


    @Test
    @DisplayName("isCompleted should return true only when VISITED")
    void isCompletedShouldReturnTrueOnlyIfVisited() {
        waypoint.setStatus(WaypointStatus.VISITED);
        assertTrue(waypoint.isCompleted());

        waypoint.setStatus(WaypointStatus.PENDING);
        assertFalse(waypoint.isCompleted());

        waypoint.setStatus(WaypointStatus.SKIPPED);
        assertFalse(waypoint.isCompleted());
    }

    @Test
    @DisplayName("canBeVisited should return true only when PENDING")
    void canBeVisitedShouldReturnTrueOnlyIfPending() {
        waypoint.setStatus(WaypointStatus.PENDING);
        assertTrue(waypoint.canBeVisited());

        waypoint.setStatus(WaypointStatus.VISITED);
        assertFalse(waypoint.canBeVisited());

        waypoint.setStatus(WaypointStatus.SKIPPED);
        assertFalse(waypoint.canBeVisited());
    }


    @Test
    @DisplayName("Should allow setting estimated arrival time and retrieve correctly")
    void shouldSetAndGetEstimatedArrivalTime() {
        LocalDateTime estimate = LocalDateTime.now().plusMinutes(30);
        waypoint.setEstimatedArrivalTime(estimate);

        assertEquals(estimate, waypoint.getEstimatedArrivalTime());
    }

    @Test
    @DisplayName("Should maintain sequence order consistency")
    void shouldMaintainSequenceOrder() {
        waypoint.setSequenceOrder(5);
        assertEquals(5, waypoint.getSequenceOrder());
    }
}
