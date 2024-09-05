package com.example.restaurant.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.restaurant.Model.Payment;
import com.example.restaurant.Repository.PaymentRepository;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository repository;

    public List<Payment> getAllPayments() {
        return repository.findAll();
    }

    public Optional<Payment> getPaymentById(Long id) {
        return repository.findById(id);
    }

    public Payment addItem(Payment payment) {
        return repository.save(payment);
    }

    public boolean deletePayment(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public Payment getPaymentByDeliveryId(Long deliveryId) {
        return repository.findByDeliveryId(deliveryId);
    }

    @Transactional
    public void deletePaymentsByDeliveryId(Long deliveryId) {
        repository.deleteByDeliveryId(deliveryId);
    }

    public List<Payment> getPaymentsByDateRange(Timestamp startDate, Timestamp endDate) {
        return repository.findByCreatedAtBetween(startDate, endDate);
    }

}
