package com.example.restaurant.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restaurant.Model.Offer;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

}
