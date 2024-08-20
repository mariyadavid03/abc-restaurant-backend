package com.example.restaurant.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restaurant.Model.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

}
