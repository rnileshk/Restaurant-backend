package com.restaurant.app.controller;

import com.restaurant.app.entity.*;
import com.restaurant.app.repository.*;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employee")
@CrossOrigin("*")
public class EmployeeController {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final BookingRepository bookingRepository;
    private final BillRepository billRepository;

    public EmployeeController(
            UserRepository userRepository,
            OrderRepository orderRepository,
            BookingRepository bookingRepository,
            BillRepository billRepository
    ) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.bookingRepository = bookingRepository;
        this.billRepository = billRepository;
    }

    /*
     =========================================
     EMPLOYEE LIST
     =========================================
    */

    @GetMapping("/all")
    public List<User> getAllEmployees() {

        return userRepository.findAll()
                .stream()
                .filter(user ->
                        user.getRole() == Role.EMPLOYEE
                                || user.getRole() == Role.BILLING_STAFF
                                || user.getRole() == Role.DELIVERY_STAFF
                )
                .toList();
    }

    /*
     =========================================
     ORDERS
     =========================================
    */

    @GetMapping("/orders")
    public List<FoodOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    @PutMapping("/orders/{id}/accept")
    public FoodOrder acceptOrder(
            @PathVariable Long id
    ) {

        FoodOrder order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found")
                );

        order.setStatus(OrderStatus.ACCEPTED);

        return orderRepository.save(order);
    }

    @PutMapping("/orders/{id}/prepare")
    public FoodOrder prepareOrder(
            @PathVariable Long id
    ) {

        FoodOrder order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found")
                );

        order.setStatus(OrderStatus.PREPARING);

        return orderRepository.save(order);
    }

    @PutMapping("/orders/{id}/ready")
    public FoodOrder readyOrder(
            @PathVariable Long id
    ) {

        FoodOrder order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found")
                );

        order.setStatus(OrderStatus.READY);

        return orderRepository.save(order);
    }

    @PutMapping("/orders/{id}/complete")
    public FoodOrder completeOrder(
            @PathVariable Long id
    ) {

        FoodOrder order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found")
                );

        order.setStatus(OrderStatus.COMPLETED);

        return orderRepository.save(order);
    }

    /*
     =========================================
     BOOKINGS
     =========================================
    */

    @GetMapping("/bookings")
    public List<TableBooking> getBookings() {
        return bookingRepository.findAll();
    }

    @PutMapping("/bookings/{id}/checkin")
    public TableBooking checkInCustomer(
            @PathVariable Long id
    ) {

        TableBooking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found")
                );

        booking.setCheckedIn(true);

        return bookingRepository.save(booking);
    }

    /*
     =========================================
     BILLING
     =========================================
    */

    @PostMapping("/billing/create")
    public Bill createBill(
            @RequestBody Bill bill
    ) {

        return billRepository.save(bill);
    }

    @GetMapping("/billing")
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    @GetMapping("/bookings/today")
    public Optional<TableBooking> getTodayBookings() {

        String today =
                LocalDate.now().toString();

        return bookingRepository.findByBookingCode(today);
    }

    /*
     =========================================
     QR BOOKING VERIFY
     =========================================
    */

    @GetMapping("/verify-booking")
    public String verifyBooking(
            @RequestParam String bookingCode
    ) {

        List<TableBooking> bookings =
                bookingRepository.findAll();

        for (TableBooking booking : bookings) {

            if (booking.getBookingCode() != null &&
                    booking.getBookingCode().equals(bookingCode)) {

                return "Booking verified for: "
                        + booking.getCustomerName();
            }
        }

        return "Invalid Booking QR";
    }
}