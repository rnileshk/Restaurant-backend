package com.restaurant.app.service;

import com.restaurant.app.entity.TableBooking;
import com.restaurant.app.entity.User;
import com.restaurant.app.repository.BookingRepository;
import com.restaurant.app.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final PdfService pdfService;
    private final EmailService emailService;

    public BookingService(
            BookingRepository bookingRepository,
            UserRepository userRepository,
            PdfService pdfService,
            EmailService emailService
    ) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.pdfService = pdfService;
        this.emailService = emailService;
    }

    public TableBooking createBooking(
            TableBooking booking,
            String email
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        booking.setUser(user);

        String bookingCode = "BOOK-" + UUID.randomUUID()
                .toString()
                .substring(0, 6)
                .toUpperCase();

        booking.setBookingCode(bookingCode);

        String verifyUrl = "http://localhost:5173/verify-booking/" + bookingCode;

        String encodedVerifyUrl = URLEncoder.encode(
                verifyUrl,
                StandardCharsets.UTF_8
        );

        booking.setQrCode(
                "http://localhost:8080/api/qr/generate?text=" + encodedVerifyUrl
        );

        booking.setCheckedIn(false);

        TableBooking savedBooking = bookingRepository.save(booking);

        try {
            emailService.sendHtmlEmail(
                savedBooking.getCustomerEmail(),
                    "Booking Confirmed - " + savedBooking.getBookingCode(),
                        savedBooking.getCustomerName(),
                        savedBooking.getBookingCode(),
                        savedBooking.getBookingDate(),
                        savedBooking.getBookingTime(),
                        savedBooking.getTotalPersons(),
                        savedBooking.getId()
            );
        } catch (Exception e) {
            System.out.println("EMAIL ERROR: " + e.getMessage());
        }

        return savedBooking;
    }

    public List<TableBooking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<TableBooking> getMyBookings(String email) {
        return bookingRepository.findByUserEmail(email);
    }

    public TableBooking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    public TableBooking getBookingByCode(String bookingCode) {
        return bookingRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    public TableBooking checkInCustomer(Long id) {
        TableBooking booking = getBookingById(id);
        booking.setCheckedIn(true);
        return bookingRepository.save(booking);
    }

    public TableBooking checkInByBookingCode(String bookingCode) {
        TableBooking booking = getBookingByCode(bookingCode);
        booking.setCheckedIn(true);
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        TableBooking booking = getBookingById(id);
        bookingRepository.delete(booking);
    }

    public byte[] generateBookingTicket(Long id) {
        TableBooking booking = getBookingById(id);
        return pdfService.generateBookingTicket(booking);
    }
}