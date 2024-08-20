package com.example.restaurant.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restaurant.Model.Gallery;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {

}
