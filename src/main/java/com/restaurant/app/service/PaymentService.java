package com.restaurant.app.service;

import com.restaurant.app.entity.Payment;
import com.restaurant.app.entity.PaymentStatus;
import com.restaurant.app.repository.PaymentRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createPayment(Payment payment) {

        if (payment.getTransactionId() == null || payment.getTransactionId().isBlank()) {
            payment.setTransactionId(
                    "TXN-" + UUID.randomUUID()
                            .toString()
                            .substring(0, 8)
                            .toUpperCase()
            );
        }

        if (payment.getPaymentStatus() == null) {
            payment.setPaymentStatus(PaymentStatus.PENDING);
        }

        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    public Payment updatePaymentStatus(Long id, PaymentStatus status) {
        Payment payment = getPaymentById(id);
        payment.setPaymentStatus(status);
        return paymentRepository.save(payment);
    }
}