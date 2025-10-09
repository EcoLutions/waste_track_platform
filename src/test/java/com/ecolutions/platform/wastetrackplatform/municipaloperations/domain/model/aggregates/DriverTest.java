package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.DriverLicense;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.DriverStatus;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DriverTest {

    private Driver driver;

    @BeforeEach
    void setUp() {
        // Arrange
        DistrictId districtId = new DistrictId("150101");
        FullName fullName = new FullName("John", "Doe");
        DocumentNumber documentNumber = new DocumentNumber("12345678");
        PhoneNumber phoneNumber = new PhoneNumber("+51987654321");
        UserId userId = new UserId("USR-001");
        DriverLicense driverLicense = new DriverLicense("A1234567");
        LocalDate expiryDate = LocalDate.now().plusYears(1);
        EmailAddress emailAddress = new EmailAddress("john.doe@example.com");

        driver = new Driver(
                districtId,
                fullName,
                documentNumber,
                phoneNumber,
                userId,
                driverLicense,
                expiryDate,
                emailAddress
        );
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
        driver.startRoute();
        int hoursWorked = 5;
        driver.setTotalHoursWorked(10);

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

    @Test
    @DisplayName("Should assign and unassign a vehicle to the driver")
    void shouldAssignAndUnassignVehicleSuccessfully() {
        // Arrange
        VehicleId vehicleId = new VehicleId("VEH-001");

        // Act
        driver.assignVehicle(vehicleId);

        // Assert
        assertEquals(vehicleId, driver.getAssignedVehicleId());

        // Act - Unassign
        driver.unassignVehicle();

        // Assert
        assertNull(driver.getAssignedVehicleId());
    }
}