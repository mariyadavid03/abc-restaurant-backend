package com.example.restaurant.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.restaurant.Model.RestaurantService;

@Repository
public interface ServiceRepository extends JpaRepository<RestaurantService, Long> {
    @Query("SELECT s.service_image_data FROM RestaurantService s WHERE s.id = :id")
    byte[] findImageById(@Param("id") Long id);
}
