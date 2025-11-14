package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.CreateNotificationCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.NotificationChannel;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.NotificationPriority;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.NotificationStatus;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.NotificationType;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.RouteId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.UserId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Notification extends AuditableAbstractAggregateRoot<Notification> {

    @NotNull
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "user_id"))
    private UserId userId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @NotNull
    private String title;

    @NotNull
    private String message;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NotificationPriority priority;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NotificationChannel channel;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "route_id"))
    private RouteId routeId;

    private LocalDateTime readAt;

    public Notification() {
        super();
    }

    public Notification(CreateNotificationCommand command) {
        this();
        this.userId = UserId.of(command.userId());
        this.type = NotificationType.fromString(command.type());
        this.title = command.title();
        this.message = command.message();
        this.priority = NotificationPriority.fromString(command.priority());
        this.status = NotificationStatus.UNREAD;
        this.channel = NotificationChannel.fromString(command.channel());
        this.routeId = RouteId.of(command.routeId());
    }

    public void markAsRead() {
        if (this.status == NotificationStatus.UNREAD) {
            this.status = NotificationStatus.READ;
            this.readAt = LocalDateTime.now();
        }
    }

    public boolean isUnread() {
        return this.status == NotificationStatus.UNREAD;
    }

    public boolean belongsToUser(String userId) {
        return this.userId.equals(new UserId(userId));
    }
}
