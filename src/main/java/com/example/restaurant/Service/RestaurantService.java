package com.example.restaurant.Service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restaurant.Model.Restaurant;
import com.example.restaurant.Repository.RestaurantRepository;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepository repository;

    public List<Restaurant> allResturants() {
        return repository.findAll();
    }

    public Optional<Restaurant> singleRestaurant(ObjectId id) {
        return repository.findById(id);
    }

    public Restaurant addRestaurant(Restaurant restaurant) {
        return repository.save(restaurant);
    }

    public Optional<Restaurant> removeRestaurant(ObjectId id) {
        Optional<Restaurant> restaurant = repository.findById(id);
        if (restaurant.isPresent()) {
            repository.deleteById(id);
        }
        return restaurant;
    }

}
