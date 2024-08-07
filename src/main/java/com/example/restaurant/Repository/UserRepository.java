package com.example.restaurant.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.restaurant.Model.User;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

}
