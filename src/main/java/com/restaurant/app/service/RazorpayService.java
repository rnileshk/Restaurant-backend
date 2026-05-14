package com.restaurant.app.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

import com.restaurant.app.dto.RazorpayOrderRequest;
import com.restaurant.app.dto.RazorpayOrderResponse;
import com.restaurant.app.dto.RazorpayVerifyRequest;
import com.restaurant.app.entity.FoodOrder;
import com.restaurant.app.entity.Payment;
import com.restaurant.app.entity.PaymentStatus;
import com.restaurant.app.repository.OrderRepository;
import com.restaurant.app.repository.PaymentRepository;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public RazorpayService(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    public RazorpayOrderResponse createRazorpayOrder(
            RazorpayOrderRequest request
    ) {
        try {
            RazorpayClient razorpayClient =
                    new RazorpayClient(keyId, keySecret);

            int amountInPaise =
                    (int) Math.round(request.getAmount() * 100);

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "order_" + request.getOrderId());

            Order order = razorpayClient.orders.create(orderRequest);

            return new RazorpayOrderResponse(
                    order.get("id"),
                    order.get("currency"),
                    order.get("amount"),
                    keyId
            );
        } catch (Exception e) {
            throw new RuntimeException(
                    "Unable to create Razorpay order: " + e.getMessage()
            );
        }
    }

    public Payment verifyAndSavePayment(
            RazorpayVerifyRequest request
    ) {
        try {
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", request.getRazorpayOrderId());
            attributes.put("razorpay_payment_id", request.getRazorpayPaymentId());
            attributes.put("razorpay_signature", request.getRazorpaySignature());

            boolean isValid =
                    Utils.verifyPaymentSignature(attributes, keySecret);

            if (!isValid) {
                throw new RuntimeException("Invalid Razorpay payment signature");
            }

            FoodOrder order = orderRepository.findById(request.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            Payment payment = Payment.builder()
                    .order(order)
                    .amount(request.getAmount())
                    .paymentMethod("RAZORPAY")
                    .transactionId(request.getRazorpayPaymentId())
                    .razorpayOrderId(request.getRazorpayOrderId())
                    .paymentStatus(PaymentStatus.SUCCESS)
                    .build();

            return paymentRepository.save(payment);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Payment verification failed: " + e.getMessage()
            );
        }
    }
}