package com.example.restaurant.Controller;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.restaurant.Model.Restaurant;
import com.example.restaurant.Service.RestaurantService;

@Controller
@RequestMapping("/restaurant")
public class RestaurantController {
    @Autowired
    private RestaurantService rService;

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        return new ResponseEntity<>(rService.allResturants(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Restaurant>> getRestaurant(@PathVariable ObjectId id) {
        return new ResponseEntity<>(rService.singleRestaurant(id), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Restaurant> addRestaurant(@RequestBody Restaurant restaurant) {
        Restaurant newRestaurant = rService.addRestaurant(restaurant);
        return new ResponseEntity<>(newRestaurant, HttpStatus.OK);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeRestaurant(@PathVariable ObjectId id) {
        Optional<Restaurant> reOptional = rService.removeRestaurant(id);
        if (reOptional.isPresent()) {
            return new ResponseEntity<>("Restaurant removed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Restaurant not found", HttpStatus.NOT_FOUND);

        }

    }

}
