package com.example.restaurant.Controller;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restaurant.Model.User;
import com.example.restaurant.Service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.allUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> getUser(@PathVariable ObjectId id) {
        return new ResponseEntity<>(userService.singleUser(id), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User newUser = userService.addUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeUser(@PathVariable ObjectId id) {
        Optional<User> user = userService.removeUser(id);
        if (user.isPresent()) {
            return new ResponseEntity<>("User removed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

}
