package com.example.restaurant.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.restaurant.Model.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByType(String type);

    @Query("SELECT m.item_image_data FROM Menu m WHERE m.id = :id")
    byte[] findImageById(@Param("id") Long id);
}
