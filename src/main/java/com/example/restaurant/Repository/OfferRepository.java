package com.example.restaurant.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.restaurant.Model.Offer;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    @Query("SELECT o.offer_image_data FROM Offer o WHERE o.id = :id")
    byte[] findImageById(@Param("id") Long id);

}
