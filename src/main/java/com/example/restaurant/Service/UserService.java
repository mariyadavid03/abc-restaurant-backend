package com.example.restaurant.Service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restaurant.Model.User;
import com.example.restaurant.Repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public Optional<User> singleUser(ObjectId id) {
        return userRepository.findById(id);
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> removeUser(ObjectId id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
        }
        return user;
    }
}
