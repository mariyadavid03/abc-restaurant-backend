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
@Table(name = "query_responses")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class QueryResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "query_id", nullable = false)
    private CustomerQuery query;

    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String response_message;

    @Column(updatable = false)
    @CreationTimestamp
    private Timestamp response_date_time;
}
