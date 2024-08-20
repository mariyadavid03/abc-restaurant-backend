package com.example.restaurant.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "offers")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String offer_name;

    @Column(nullable = false)
    private String offer_desc;

    @Column
    private String discount;

    @Column
    private String valid_period;

    @Column
    private byte[] offer_image_data;
}
