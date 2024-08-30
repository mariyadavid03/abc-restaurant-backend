package com.example.restaurant.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.restaurant.Model.DineInReservation;

@Repository
public interface DineInRepository extends JpaRepository<DineInReservation, Long> {
    @Query("SELECT r FROM DineInReservation r WHERE r.user.id = :id")
    List<DineInReservation> getAllReservationUser(@Param("id") Long id);

}
