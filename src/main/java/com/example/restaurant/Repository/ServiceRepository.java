package com.example.restaurant.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restaurant.Model.RestaurantService;

@Repository
public interface ServiceRepository extends JpaRepository<RestaurantService, Long> {

}
