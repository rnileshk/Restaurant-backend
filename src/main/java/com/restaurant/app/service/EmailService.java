package com.restaurant.app.service;

import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(
            JavaMailSender mailSender
    ) {
        this.mailSender = mailSender;
    }

    /*
     =====================================
     SEND HTML EMAIL
     =====================================
    */

    public void sendHtmlEmail(
            String to,
            String subject,
            String customerName,
            String bookingCode,
            String bookingDate,
            String bookingTime,
            Integer persons,
            Long bookingId
    ) {

        try {

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true
                    );

            helper.setFrom(fromEmail);

            helper.setTo(to);

            helper.setSubject(subject);

            String html = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <style>

                            body {
                                font-family: Arial, sans-serif;
                                background: #f5f5f5;
                                padding: 20px;
                            }

                            .container {
                                max-width: 650px;
                                margin: auto;
                                background: white;
                                border-radius: 12px;
                                overflow: hidden;
                                box-shadow: 0 0 20px rgba(0,0,0,0.1);
                            }

                            .header {
                                background: #111827;
                                color: white;
                                padding: 30px;
                                text-align: center;
                            }

                            .content {
                                padding: 30px;
                            }

                            .booking-box {
                                background: #f9fafb;
                                border-radius: 10px;
                                padding: 20px;
                                margin-top: 20px;
                            }

                            .footer {
                                background: #111827;
                                color: white;
                                text-align: center;
                                padding: 15px;
                                font-size: 14px;
                            }

                            .button {
                                display: inline-block;
                                margin-top: 20px;
                                background: #2563eb;
                                color: white !important;
                                padding: 12px 22px;
                                text-decoration: none;
                                border-radius: 8px;
                                font-weight: bold;
                            }

                            .highlight {
                                color: #2563eb;
                                font-weight: bold;
                            }

                        </style>
                    </head>

                    <body>

                        <div class="container">

                            <div class="header">
                                <h1>🍽 Restaurant Booking Confirmed</h1>
                                <p>Your table has been reserved successfully</p>
                            </div>

                            <div class="content">

                                <h2>Hello %s 👋</h2>

                                <p>
                                    Thank you for booking with us.
                                    Your reservation has been confirmed.
                                </p>

                                <div class="booking-box">

                                    <p>
                                        <strong>Booking Code:</strong>
                                        <span class="highlight">%s</span>
                                    </p>

                                    <p>
                                        <strong>Date:</strong>
                                        %s
                                    </p>

                                    <p>
                                        <strong>Time:</strong>
                                        %s
                                    </p>

                                    <p>
                                        <strong>Total Persons:</strong>
                                        %d
                                    </p>

                                </div>

                                <a class="button"
                                   href="http://localhost:8080/api/bookings/%d/ticket">

                                    Download Booking Ticket

                                </a>

                            </div>

                            <div class="footer">
                                © 2026 Restaurant Management System
                            </div>

                        </div>

                    </body>
                    </html>
                    """.formatted(
                    customerName,
                    bookingCode,
                    bookingDate,
                    bookingTime,
                    persons,
                    bookingId
            );

            helper.setText(
                    html,
                    true
            );

            mailSender.send(message);

            System.out.println(
                    "HTML EMAIL SENT SUCCESSFULLY"
            );

        } catch (Exception e) {

            System.out.println(
                    "EMAIL ERROR: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}