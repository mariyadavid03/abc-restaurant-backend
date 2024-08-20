package com.example.restaurant.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restaurant.Model.DeliveryReservation;

@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryReservation, Long> {

}
