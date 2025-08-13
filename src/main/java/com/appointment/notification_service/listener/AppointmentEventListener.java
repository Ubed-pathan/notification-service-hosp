package com.appointment.notification_service.listener;

import com.appointment.notification_service.events.AppointmentCreatedEvent;
import com.appointment.notification_service.services.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentEventListener {

    private final MailService mailService;

    @KafkaListener(
            topics = "appointments.created",
            groupId = "${KAFKA_GROUP_ID}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleAppointmentCreated(AppointmentCreatedEvent event) {
        if (event.userEmail() == null || event.userEmail().isBlank()) {
            System.out.println("⚠ No email provided for user " + event.userId());
            return;
        }

        String subject = "Appointment Confirmation - " + event.appointmentId();
        String body = String.format(
                "Hello %s,\n\nYour appointment with Doctor %s is confirmed for %s.\nReason: %s\n\nRegards,\nClinic Team",
                event.usersFullName(),
                event.doctorFullName(),
                event.appointmentTime(),
                event.reason()
        );

        mailService.sendMail(event.userEmail(), subject, body);
//        System.out.println("✅ Email sent to: " + event.userEmail());
    }
}
