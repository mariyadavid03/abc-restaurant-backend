package com.example.restaurant.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
    @Autowired
    JavaMailSender javaMailSender;

    private Map<String, String> otpStorage = new HashMap<>();

    @PostMapping("/request-otp")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is required");
        }

        String otp = String.valueOf(new Random().nextInt(999999));
        otpStorage.put(email, otp);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("abcrestaurant@gmail.com");
        message.setTo(email);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp);
        javaMailSender.send(message);

        return ResponseEntity.ok("OTP sent to your email");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        if (otpStorage.containsKey(email) && otpStorage.get(email).equals(otp)) {
            return ResponseEntity.ok("OTP verified");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
        }
    }

}