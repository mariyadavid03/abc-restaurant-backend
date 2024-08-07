package com.example.restaurant.Model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "restaurant")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {
    @Id
    private ObjectId id;
    private int restaurant_id;
    private String location;
    private String address;
}
