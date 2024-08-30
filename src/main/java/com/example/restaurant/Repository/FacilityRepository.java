package com.example.restaurant.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.restaurant.Model.Facility;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    @Query("SELECT f.service_image_data FROM Facility f WHERE f.id = :id")
    byte[] findImageById(@Param("id") Long id);
}
