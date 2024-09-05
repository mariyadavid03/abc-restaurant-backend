package com.example.restaurant.Repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restaurant.Model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByDeliveryId(Long deliveryId);

    void deleteByDeliveryId(Long deliveryId);

    List<Payment> findByCreatedAtBetween(Timestamp startDate, Timestamp endDate);
}
