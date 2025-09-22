package com.appointment.notification_service.events;

import java.time.LocalDateTime;

public record AppointmentCreatedEvent(
        String appointmentId,
        String userId,
        String usersFullName,
        String userEmail,
        String doctorFullName,
        LocalDateTime appointmentStartTime,
        String reason
) {}