package com.restaurant.app.service;

import com.restaurant.app.entity.Bill;
import com.restaurant.app.entity.FoodOrder;
import com.restaurant.app.entity.PaymentStatus;
import com.restaurant.app.entity.TableBooking;

import com.restaurant.app.repository.BillRepository;
import com.restaurant.app.repository.BookingRepository;
import com.restaurant.app.repository.OrderRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillingService {

    private final BillRepository billRepository;
    private final OrderRepository orderRepository;
    private final BookingRepository bookingRepository;

    public BillingService(
            BillRepository billRepository,
            OrderRepository orderRepository,
            BookingRepository bookingRepository
    ) {
        this.billRepository = billRepository;
        this.orderRepository = orderRepository;
        this.bookingRepository = bookingRepository;
    }

    /*
     =====================================
     CREATE NORMAL BILL
     =====================================
    */

    public Bill createBill(
            Bill bill
    ) {

        double subtotal =
                bill.getSubtotal() != null
                        ? bill.getSubtotal()
                        : 0.0;

        double tax =
                bill.getTax() != null
                        ? bill.getTax()
                        : 0.0;

        double deliveryCharge =
                bill.getDeliveryCharge() != null
                        ? bill.getDeliveryCharge()
                        : 0.0;

        double total =
                subtotal
                        + tax
                        + deliveryCharge;

        bill.setTotalAmount(total);

        if (bill.getPaymentStatus() == null) {
            bill.setPaymentStatus(
                    PaymentStatus.PENDING
            );
        }

        return billRepository.save(bill);
    }

    /*
     =====================================
     GET ALL BILLS
     =====================================
    */

    public List<Bill> getAllBills() {

        return billRepository.findAll();
    }

    /*
     =====================================
     CREATE BILL FOR ORDER
     =====================================
    */

    public Bill createBillForOrder(
            Long orderId
    ) {

        FoodOrder order =
                orderRepository.findById(orderId)
                        .orElseThrow(() ->

                                new RuntimeException(
                                        "Order not found"
                                )
                        );

        double subtotal =
                order.getTotalAmount() != null
                        ? order.getTotalAmount()
                        : 0.0;

        double tax =
                subtotal * 0.05;

        double deliveryCharge = 40.0;

        Bill bill = Bill.builder()

                .order(order)

                .subtotal(subtotal)

                .tax(tax)

                .deliveryCharge(deliveryCharge)

                .totalAmount(
                        subtotal
                                + tax
                                + deliveryCharge
                )

                .paymentStatus(
                        PaymentStatus.PENDING
                )

                .build();

        return billRepository.save(bill);
    }

    /*
     =====================================
     CREATE BILL FOR BOOKING
     =====================================
    */

    public Bill createBillForBooking(
            Long bookingId
    ) {

        TableBooking booking =
                bookingRepository.findById(bookingId)
                        .orElseThrow(() ->

                                new RuntimeException(
                                        "Booking not found"
                                )
                        );

        double subtotal = 500.0;

        double tax = 25.0;

        double deliveryCharge = 0.0;

        Bill bill = Bill.builder()

                .booking(booking)

                .subtotal(subtotal)

                .tax(tax)

                .deliveryCharge(deliveryCharge)

                .totalAmount(
                        subtotal
                                + tax
                                + deliveryCharge
                )

                .paymentStatus(
                        PaymentStatus.PENDING
                )

                .build();

        return billRepository.save(bill);
    }
}