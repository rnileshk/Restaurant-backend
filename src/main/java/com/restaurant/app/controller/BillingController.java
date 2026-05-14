package com.restaurant.app.controller;

import com.restaurant.app.entity.Bill;
import com.restaurant.app.service.BillingService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Bill> createBill(
            @RequestBody Bill bill
    ) {

        return ResponseEntity.ok(
                billingService.createBill(bill)
        );
    }

    @GetMapping
    public ResponseEntity<List<Bill>> getAllBills() {

        return ResponseEntity.ok(
                billingService.getAllBills()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBillById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                billingService.getBillById(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBill(
            @PathVariable Long id
    ) {

        billingService.deleteBill(id);

        return ResponseEntity.ok(
                "Bill deleted successfully"
        );
    }
}