package com.example.restaurant.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restaurant.Model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
