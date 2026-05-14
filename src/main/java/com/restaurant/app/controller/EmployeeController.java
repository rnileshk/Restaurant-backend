package com.restaurant.app.controller;

import com.restaurant.app.entity.*;
import com.restaurant.app.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeController(
            UserRepository userRepository,
            OrderRepository orderRepository,
            BookingRepository bookingRepository,
            BillRepository billRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.bookingRepository = bookingRepository;
        this.billRepository = billRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/all")
    public List<User> getAllEmployees() {
        return userRepository.findAll()
                .stream()
                .filter(user ->
                        user.getRole() == Role.EMPLOYEE ||
                        user.getRole() == Role.BILLING_STAFF ||
                        user.getRole() == Role.DELIVERY_STAFF
                )
                .toList();
    }

    @PutMapping("/{id}")
    public User updateEmployee(
            @PathVariable Long id,
            @RequestBody User updatedEmployee
    ) {
        User employee = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employee.setName(updatedEmployee.getName());
        employee.setEmail(updatedEmployee.getEmail());
        employee.setPhone(updatedEmployee.getPhone());
        employee.setRole(updatedEmployee.getRole());

        if (updatedEmployee.getActive() != null) {
            employee.setActive(updatedEmployee.getActive());
        }

        if (
                updatedEmployee.getPassword() != null &&
                !updatedEmployee.getPassword().isBlank()
        ) {
            employee.setPassword(
                    passwordEncoder.encode(updatedEmployee.getPassword())
            );
        }

        return userRepository.save(employee);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        User employee = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        refreshTokenRepository.deleteByUser(employee);
        userRepository.delete(employee);

        return "Employee deleted successfully";
    }

    @GetMapping("/orders")
    public List<FoodOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    @PutMapping("/orders/{id}/accept")
    public FoodOrder acceptOrder(@PathVariable Long id) {
        FoodOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.ACCEPTED);
        return orderRepository.save(order);
    }

    @PutMapping("/orders/{id}/prepare")
    public FoodOrder prepareOrder(@PathVariable Long id) {
        FoodOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.PREPARING);
        return orderRepository.save(order);
    }

    @PutMapping("/orders/{id}/ready")
    public FoodOrder readyOrder(@PathVariable Long id) {
        FoodOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.READY);
        return orderRepository.save(order);
    }

    @PutMapping("/orders/{id}/complete")
    public FoodOrder completeOrder(@PathVariable Long id) {
        FoodOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.COMPLETED);
        return orderRepository.save(order);
    }

    @GetMapping("/bookings")
    public List<TableBooking> getBookings() {
        return bookingRepository.findAll();
    }

    @PutMapping("/bookings/{id}/checkin")
    public TableBooking checkInCustomer(@PathVariable Long id) {
        TableBooking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setCheckedIn(true);
        return bookingRepository.save(booking);
    }

    @PostMapping("/billing/create")
    public Bill createBill(@RequestBody Bill bill) {
        return billRepository.save(bill);
    }

    @GetMapping("/billing")
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    @GetMapping("/bookings/today")
    public Optional<TableBooking> getTodayBookings() {
        String today = LocalDate.now().toString();
        return bookingRepository.findByBookingCode(today);
    }

    @GetMapping("/verify-booking")
    public String verifyBooking(@RequestParam String bookingCode) {
        List<TableBooking> bookings = bookingRepository.findAll();

        for (TableBooking booking : bookings) {
            if (
                    booking.getBookingCode() != null &&
                    booking.getBookingCode().equals(bookingCode)
            ) {
                return "Booking verified for: " + booking.getCustomerName();
            }
        }

        return "Invalid Booking QR";
    }
}