package com.restaurant.app.controller;

import com.restaurant.app.entity.Bill;
import com.restaurant.app.service.BillingService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing")
@CrossOrigin("*")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @PostMapping
    public Bill createBill(@RequestBody Bill bill) {
        return billingService.createBill(bill);
    }

    @GetMapping
    public List<Bill> getBills() {
        return billingService.getAllBills();
    }

    @PostMapping("/order/{orderId}")
    public Bill createOrderBill(
        @PathVariable Long orderId
    ) {
        return billingService.createBillForOrder(orderId);
    }

    @PostMapping("/booking/{bookingId}")
    public Bill createBookingBill(
        @PathVariable Long bookingId
    ) {
        return billingService.createBillForBooking(bookingId);
    }
}