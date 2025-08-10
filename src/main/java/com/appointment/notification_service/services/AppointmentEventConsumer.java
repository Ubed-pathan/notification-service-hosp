package com.appointment.notification_service.services;

import com.appointment.notification_service.events.AppointmentCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentEventConsumer {

    private final JavaMailSender mailSender;

    @KafkaListener(topics = "appointments.created", groupId = "notification-service-group")
    public void consume(AppointmentCreatedEvent event) {
        System.out.println("📩 Received appointment event for user: " + event.usersFullName());
        sendAppointmentEmail(event);
    }

    private void sendAppointmentEmail(AppointmentCreatedEvent event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.userEmail());
        message.setSubject("Your Appointment Confirmation");
        message.setText(String.format("""
                Hello %s,
                
                Your appointment with Doctor %s is confirmed for %s.
                Reason: %s
                
                Appointment ID: %s
                
                Regards,
                YourApp Team
                """, event.usersFullName(), event.doctorId(), event.appointmentTime(), event.reason(), event.appointmentId()));

        mailSender.send(message);
        System.out.println("✅ Email sent to " + event.userEmail());
    }
}
