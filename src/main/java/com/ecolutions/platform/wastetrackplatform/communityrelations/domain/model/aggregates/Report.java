package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.valueobjects.ReportStatus;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.valueobjects.ReportType;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Report extends AuditableAbstractAggregateRoot<Report> {

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "citizen_id", nullable = false))
    private CitizenId citizenId;

    @Embedded
    private Location location;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "container_id"))
    private ContainerId containerId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    @Column(columnDefinition = "TEXT")
    private String resolutionNote;

    private LocalDateTime resolvedAt;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "resolved_by"))
    private UserId resolvedBy;

    @NotNull
    private LocalDateTime submittedAt;

    private LocalDateTime acknowledgedAt;

    public Report() {
        super();
        this.status = ReportStatus.SUBMITTED;
        this.submittedAt = LocalDateTime.now();
    }

    public Report(CitizenId citizenId, Location location, ReportType reportType,
                  String description) {
        this();
        this.citizenId = citizenId;
        this.location = location;
        this.reportType = reportType;
        this.description = description;
    }

    public void acknowledge() {
        if (this.status != ReportStatus.SUBMITTED) {
            throw new IllegalArgumentException("Only submitted reports can be acknowledged");
        }

        this.status = ReportStatus.ACKNOWLEDGED;
        this.acknowledgedAt = LocalDateTime.now();
    }

    public void startProcessing() {
        if (this.status != ReportStatus.ACKNOWLEDGED) {
            throw new IllegalArgumentException("Only acknowledged reports can start processing");
        }

        this.status = ReportStatus.IN_PROGRESS;
    }

    public void resolve(String note, UserId resolvedBy) {
        if (note == null || note.isBlank()) {
            throw new IllegalArgumentException("Resolution note cannot be null or blank");
        }
        if (resolvedBy == null) {
            throw new IllegalArgumentException("Resolver cannot be null");
        }
        if (this.status != ReportStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Only in-progress reports can be resolved");
        }

        this.status = ReportStatus.RESOLVED;
        this.resolutionNote = note;
        this.resolvedAt = LocalDateTime.now();
        this.resolvedBy = resolvedBy;
    }

    public void reject(String reason, UserId rejectedBy) {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Rejection reason cannot be null or blank");
        }
        if (rejectedBy == null) {
            throw new IllegalArgumentException("Rejector cannot be null");
        }
        if (this.status == ReportStatus.RESOLVED || this.status == ReportStatus.REJECTED) {
            throw new IllegalArgumentException("Cannot reject already closed reports");
        }

        this.status = ReportStatus.REJECTED;
        this.resolutionNote = reason;
        this.resolvedAt = LocalDateTime.now();
        this.resolvedBy = rejectedBy;
    }

    public Duration calculateResolutionTime() {
        if (this.resolvedAt == null) {
            return null;
        }

        LocalDateTime startTime = this.submittedAt;
        if (this.acknowledgedAt != null) {
            startTime = this.acknowledgedAt;
        }

        return Duration.between(startTime, this.resolvedAt);
    }

    public boolean isOverdue() {
        if (this.status.isClosed()) {
            return false;
        }

        Duration maxTime = this.status.getMaxResolutionTime();
        LocalDateTime startTime = this.submittedAt;

        if (this.acknowledgedAt != null) {
            startTime = this.acknowledgedAt;
        }

        return Duration.between(startTime, LocalDateTime.now()).compareTo(maxTime) > 0;
    }
}
