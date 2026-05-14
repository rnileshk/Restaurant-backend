package com.restaurant.app.service;

import com.restaurant.app.entity.Bill;
import com.restaurant.app.repository.BillRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BillingService {

    private final BillRepository billRepository;

    public BillingService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public Bill createBill(Bill bill) {

        bill.setCreatedAt(LocalDateTime.now());

        return billRepository.save(bill);
    }

    public List<Bill> getAllBills() {

        return billRepository.findAll();
    }

    public Bill getBillById(Long id) {

        return billRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Bill not found"));
    }

    public void deleteBill(Long id) {

        if (!billRepository.existsById(id)) {
            throw new RuntimeException("Bill not found");
        }

        billRepository.deleteById(id);
    }
}