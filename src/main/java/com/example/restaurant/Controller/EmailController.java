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

    @PostMapping("/query-response")
    public ResponseEntity<String> sendResponse(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String response = request.get("response");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("abcrestaurant@gmail.com");
        message.setTo(email);
        message.setSubject("Your Query Response");
        message.setText(response);
        javaMailSender.send(message);

        return ResponseEntity.ok("Response Sent");

    }

    @PostMapping("/sendReservationEmail")
    public ResponseEntity<String> sendReservationEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String reservationCode = request.get("reservation_code");
        String reservationDateTime = request.get("reservation_date_time");
        String numGuests = request.get("num_guests");
        String specialRequests = request.get("special_requests");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("abcrestaurant@gmail.com");
        message.setTo(email);
        message.setSubject("Confirmation of Your Reservation");
        String emailContent = "Dear Customer,\n\n" +
                "Thank you for your reservation. We are pleased to confirm that your reservation at ABC Restaurant have been successfully placed.\n\n"
                +
                "Here are the details:\n\n" +
                "Reservation Code: " + reservationCode + "\n" +
                "Date & Time " + reservationDateTime + "\n" +
                "Number of Diners " + numGuests + "\n\n" +
                "If you have any questions or need further assistance, please do not hesitate to contact us.\n\n" +
                "Best regards,\n" +
                "The ABC Restaurant Team";
        message.setText(emailContent);

        javaMailSender.send(message);
        return ResponseEntity.ok("Reservation Details Sent");
    }

    @PostMapping("/sendPaymentEmail")
    public ResponseEntity<String> sendPaymentEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String deliveryCode = request.get("code");
        String totalPrice = request.get("amount");
        String paymentTime = request.get("createdAt");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("abcrestaurant@gmail.com");
        message.setTo(email);
        message.setSubject("Confirmation of Your Delivery and Payment");
        String emailContent = "Dear Customer,\n\n" +
                "Thank you for your order. We are pleased to confirm that your delivery and payment have been successfully processed.\n\n"
                +
                "Here are the details of your transaction:\n\n" +
                "Delivery Code: " + deliveryCode + "\n" +
                "Total Amount: LKR " + totalPrice + "\n" +
                "Payment Date & Time: " + paymentTime + "\n\n" +
                "If you have any questions or need further assistance, please do not hesitate to contact us.\n\n" +
                "Best regards,\n" +
                "The ABC Restaurant Team";
        message.setText(emailContent);

        javaMailSender.send(message);
        return ResponseEntity.ok("Delivery & Payment Details Sent");
    }

}