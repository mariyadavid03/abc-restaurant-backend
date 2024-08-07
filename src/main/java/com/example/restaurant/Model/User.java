package com.example.restaurant.Model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private ObjectId id;
    private int user_id;
    private String username;
    private String password;
    private String role;
    private String f_name;
    private String l_name;
    private String email;
    private String mobile_no;
}
