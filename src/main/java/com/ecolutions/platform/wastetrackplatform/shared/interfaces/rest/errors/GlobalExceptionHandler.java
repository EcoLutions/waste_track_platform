package com.ecolutions.platform.wastetrackplatform.shared.interfaces.rest.errors;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String code, String message) {
        return ResponseEntity.status(status).body(new ErrorResponse(code, message, status.value(), LocalDateTime.now()));
    }

    @ExceptionHandler(RouteNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(RouteNotFoundException ex) {
        log.warn("Route not found: {}", ex.getMessage());
        return build(HttpStatus.NOT_FOUND, "ROUTE_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(RouteModificationException.class)
    public ResponseEntity<ErrorResponse> handleModification(RouteModificationException ex) {
        log.warn("Route modification error: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, "ROUTE_MODIFICATION_ERROR", ex.getMessage());
    }

    @ExceptionHandler(WaypointInsertionException.class)
    public ResponseEntity<ErrorResponse> handleWaypointInsertError(WaypointInsertionException ex) {
        log.warn("Waypoint insertion error: {}", ex.getMessage());
        return build(HttpStatus.CONFLICT, "WAYPOINT_INSERTION_ERROR", ex.getMessage());
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ErrorResponse> handleBusinessValidation(BusinessValidationException ex) {
        log.warn("Business validation error: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, "BUSINESS_VALIDATION_ERROR", ex.getMessage());
    }

    @ExceptionHandler(DriverScheduleConflictException.class)
    public ResponseEntity<ErrorResponse> handleDriverConflict(DriverScheduleConflictException ex) {
        log.warn("Driver schedule conflict: {}", ex.getMessage());
        return build(HttpStatus.CONFLICT, "DRIVER_SCHEDULE_CONFLICT", ex.getMessage());
    }

    @ExceptionHandler(VehicleScheduleConflictException.class)
    public ResponseEntity<ErrorResponse> handleVehicleConflict(VehicleScheduleConflictException ex) {
        log.warn("Vehicle schedule conflict: {}", ex.getMessage());
        return build(HttpStatus.CONFLICT, "VEHICLE_SCHEDULE_CONFLICT", ex.getMessage());
    }

    @ExceptionHandler(DistrictConfigurationException.class)
    public ResponseEntity<ErrorResponse> handleDistrictConfig(DistrictConfigurationException ex) {
        log.warn("District configuration error: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, "DISTRICT_CONFIGURATION_ERROR", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArg(IllegalArgumentException ex) {
        log.warn("Invalid argument: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, "INVALID_ARGUMENT", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnhandled(Exception ex) {
        log.error("Unhandled exception", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Unexpected server error");
    }
}