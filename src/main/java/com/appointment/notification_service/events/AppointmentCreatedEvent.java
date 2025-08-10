package com.appointment.notification_service.events;

import java.time.LocalDateTime;

public record AppointmentCreatedEvent(
        String appointmentId,
        String userId,
        String usersFullName,
        String userEmail,
        String doctorId,
        LocalDateTime appointmentTime,
        String reason
) {}