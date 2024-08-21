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
@Table(name = "delivery_reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String delivery_code;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String delivery_address;

    @Column
    private Timestamp delivery_date_time;

    @Column
    private String special_instructions;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp created_at;
}