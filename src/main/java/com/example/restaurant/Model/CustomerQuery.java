package com.example.restaurant.Model;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

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

@Entity
@Table(name = "customer_queries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerQuery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String query_subject;

    @Column(nullable = false)
    private String query_message;

    @Column
    private String status = "Pending";

    @Column(updatable = false)
    @CreationTimestamp
    private Timestamp created_at;
}