package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.CreateDriverCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.DriverStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DriverTest {

    private Driver driver;

    @BeforeEach
    void setUp() {
        // Arrange
        CreateDriverCommand createCommand = new CreateDriverCommand(
                "150101",
                "John",
                "Doe",
                "12345678",
                "+51987654321",
                "USR-001",
                "A1234567",
                LocalDate.now().plusYears(1),
                "john.doe@example.com"
        );

        driver = new Driver(createCommand);
    }

    @Test
    @DisplayName("Should start a route when driver is available")
    void shouldStartRouteWhenDriverIsAvailable() {
        // Act
        driver.startRoute();

        // Assert
        assertEquals(DriverStatus.ON_ROUTE, driver.getStatus());
    }

    @Test
    @DisplayName("Should complete route and update total hours and last route timestamp")
    void shouldCompleteRouteAndUpdateHours() {
        // Arrange
        driver.setTotalHoursWorked(10);
        driver.startRoute();
        int hoursWorked = 5;

        // Act
        driver.completeRoute(hoursWorked);

        // Assert
        assertEquals(DriverStatus.AVAILABLE, driver.getStatus());
        assertEquals(15, driver.getTotalHoursWorked());
        assertNotNull(driver.getLastRouteCompletedAt());
    }

    @Test
    @DisplayName("Should suspend driver when reason is valid")
    void shouldSuspendDriverWithValidReason() {
        // Act
        driver.suspend("Driver violated safety protocol");

        // Assert
        assertEquals(DriverStatus.SUSPENDED, driver.getStatus());
    }
}