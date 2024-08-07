package com.example.restaurant.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.restaurant.Model.Restaurant;

@Repository
public interface RestaurantRepository extends MongoRepository<Restaurant, ObjectId> {

}
