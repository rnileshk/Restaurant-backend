package com.restaurant.app.controller;

import com.restaurant.app.entity.TableBooking;
import com.restaurant.app.service.BookingService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin("*")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public TableBooking createBooking(
            @RequestBody TableBooking booking,
            Authentication authentication
    ) {
        return bookingService.createBooking(
            booking,
            authentication.getName()
        );
    }

    @GetMapping
    public List<TableBooking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/verify/{bookingCode}")
    public TableBooking verifyBooking(@PathVariable String bookingCode) {
        return bookingService.getBookingByCode(bookingCode);
    }

    @PutMapping("/checkin/{bookingCode}")
    public TableBooking checkInByBookingCode(@PathVariable String bookingCode) {
        return bookingService.checkInByBookingCode(bookingCode);
    }

    @GetMapping("/{id}")
    public TableBooking getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    @PutMapping("/{id}/checkin")
    public TableBooking checkInCustomer(@PathVariable Long id) {
        return bookingService.checkInCustomer(id);
    }

    @GetMapping("/{id}/ticket")
    public ResponseEntity<byte[]> downloadTicket(@PathVariable Long id) {

        byte[] pdf = bookingService.generateBookingTicket(id);

        return ResponseEntity.ok()
                .header(
                        "Content-Disposition",
                        "attachment; filename=booking-ticket-" + id + ".pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
   
    @DeleteMapping("/{id}")
    public String deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return "Booking deleted successfully";
    }

    @GetMapping("/my")
    public List<TableBooking> getMyBookings(
            Authentication authentication
    ) {
        return bookingService.getMyBookings(
                authentication.getName()
        );
    }
}