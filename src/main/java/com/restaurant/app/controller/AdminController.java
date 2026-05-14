package com.restaurant.app.controller;

import com.restaurant.app.entity.*;

import com.restaurant.app.repository.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminController {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final BillRepository billRepository;
    private final DeliveryRepository deliveryRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    public AdminController(
            UserRepository userRepository,
            OrderRepository orderRepository,
            BookingRepository bookingRepository,
            PaymentRepository paymentRepository,
            BillRepository billRepository,
            DeliveryRepository deliveryRepository,
            PasswordEncoder passwordEncoder,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.billRepository = billRepository;
        this.deliveryRepository = deliveryRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /*
     =====================================
     DASHBOARD STATS
     =====================================
    */

    @GetMapping("/dashboard")
    public Map<String, Object> getDashboardStats() {

        Map<String, Object> stats =
                new HashMap<>();

        stats.put(
                "totalUsers",
                userRepository.count()
        );

        stats.put(
                "totalOrders",
                orderRepository.count()
        );

        stats.put(
                "totalBookings",
                bookingRepository.count()
        );

        stats.put(
                "totalPayments",
                paymentRepository.count()
        );

        stats.put(
                "totalBills",
                billRepository.count()
        );

        stats.put(
                "totalDeliveries",
                deliveryRepository.count()
        );

        /*
         =====================================
         TOTAL REVENUE
         =====================================
        */

        double revenue = billRepository.findAll()
                .stream()
                .mapToDouble(bill ->

                        bill.getTotalAmount() != null
                                ? bill.getTotalAmount()
                                : 0.0
                )
                .sum();

        stats.put(
                "totalRevenue",
                revenue
        );

        return stats;
    }

    /*
     =====================================
     ALL USERS
     =====================================
    */

    @GetMapping("/users")
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    /*
     =====================================
     ALL ORDERS
     =====================================
    */

    @GetMapping("/orders")
    public List<FoodOrder> getAllOrders() {

        return orderRepository.findAll();
    }

    /*
     =====================================
     ALL BOOKINGS
     =====================================
    */

    @GetMapping("/bookings")
    public List<TableBooking> getAllBookings() {

        return bookingRepository.findAll();
    }

    /*
     =====================================
     ALL PAYMENTS
     =====================================
    */

    @GetMapping("/payments")
    public List<Payment> getAllPayments() {

        return paymentRepository.findAll();
    }

    /*
     =====================================
     ALL BILLS
     =====================================
    */

    @GetMapping("/bills")
    public List<Bill> getAllBills() {

        return billRepository.findAll();
    }

    /*
     =====================================
     ALL DELIVERIES
     =====================================
    */

    @GetMapping("/deliveries")
    public List<Delivery> getAllDeliveries() {

        return deliveryRepository.findAll();
    }

    @PutMapping("/users/{id}")
public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));

    user.setName(updatedUser.getName());
    user.setEmail(updatedUser.getEmail());
    user.setPhone(updatedUser.getPhone());
    user.setRole(updatedUser.getRole());
    user.setActive(updatedUser.getActive());

    if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
    }

    return userRepository.save(user);
}

@Transactional
@DeleteMapping("/users/{id}")
public String deleteUser(@PathVariable Long id) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));

    refreshTokenRepository.deleteByUser(user);

    userRepository.delete(user);

    return "User deleted successfully";
}
}