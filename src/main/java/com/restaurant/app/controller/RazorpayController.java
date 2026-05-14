package com.restaurant.app.controller;

import com.restaurant.app.dto.RazorpayOrderRequest;
import com.restaurant.app.dto.RazorpayOrderResponse;
import com.restaurant.app.dto.RazorpayVerifyRequest;
import com.restaurant.app.entity.Payment;
import com.restaurant.app.service.RazorpayService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/razorpay")
@CrossOrigin(origins = "*")
public class RazorpayController {

    private final RazorpayService razorpayService;

    public RazorpayController(RazorpayService razorpayService) {
        this.razorpayService = razorpayService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<RazorpayOrderResponse> createOrder(
            @RequestBody RazorpayOrderRequest request
    ) {
        return ResponseEntity.ok(
                razorpayService.createRazorpayOrder(request)
        );
    }

    @PostMapping("/verify-payment")
    public ResponseEntity<Payment> verifyPayment(
            @RequestBody RazorpayVerifyRequest request
    ) {
        return ResponseEntity.ok(
                razorpayService.verifyAndSavePayment(request)
        );
    }
}