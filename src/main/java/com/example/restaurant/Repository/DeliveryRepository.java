package com.example.restaurant.Repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.restaurant.Model.Delivery;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    @Query("SELECT d FROM Delivery d WHERE d.user.id = :id")
    List<Delivery> getAllDeliveryUser(@Param("id") Long id);

    List<Delivery> findByCreatedAtBetween(Timestamp startDate, Timestamp endDate);

}
