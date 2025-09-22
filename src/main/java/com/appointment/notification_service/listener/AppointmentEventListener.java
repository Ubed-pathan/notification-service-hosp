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
                event.appointmentStartTime(),
                event.reason()
        );

        mailService.sendMail(event.userEmail(), subject, body);
    }



    @KafkaListener(
            topics = "appointments.cancelled",
            groupId = "${KAFKA_GROUP_ID}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleAppointmentCancelled(AppointmentCreatedEvent event) {
        if (event.userEmail() == null || event.userEmail().isBlank()) {
            return;
        }

        String subject = "Appointment Cancelled - " + event.appointmentId();
        String body = String.format(
                "Hello %s,\n\nYour appointment with Doctor %s scheduled for %s has been cancelled.\nReason: %s\n\nRegards,\nClinic Team",
                event.usersFullName(),
                event.doctorFullName(),
                event.appointmentStartTime(),
                event.reason()
        );

        mailService.sendMail(event.userEmail(), subject, body);
    }




    @KafkaListener(
            topics = "appointments.completed",
            groupId = "${KAFKA_GROUP_ID}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleAppointmentCompleted(AppointmentCreatedEvent event) {
        if (event.userEmail() == null || event.userEmail().isBlank()) {
            return;
        }

        String subject = "Appointment Completed - " + event.appointmentId();
        String body = String.format(
                "Hello %s,\n\nWe hope you had a positive experience during your appointment with Dr. %s on %s.\n\nYour appointment has been marked as completed.\nReason: %s\n\nWe truly appreciate your trust in our clinic.\n\nTo help us improve and assist other patients, please consider leaving a review for your doctor in the appointment tab on the Hospilink application.\n\nThank you for choosing us!\nBest regards,\nClinic Team",
                event.usersFullName(),
                event.doctorFullName(),
                event.appointmentStartTime(),
                event.reason()
        );

        mailService.sendMail(event.userEmail(), subject, body);
    }
}
