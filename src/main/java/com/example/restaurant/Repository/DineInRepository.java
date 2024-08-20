package com.example.restaurant.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restaurant.Model.DineInReservation;

@Repository
public interface DineInRepository extends JpaRepository<DineInReservation, Long> {

}
