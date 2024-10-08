package com.example.restaurant.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "dine_in_reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DineInReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String reservation_code;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "reservation_date_time", nullable = false)
    private Timestamp reservationDateTime;

    @Column(nullable = false)
    private Integer num_guests;

    @Column
    private String special_requests;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp created_at;
}