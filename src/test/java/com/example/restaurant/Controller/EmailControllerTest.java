package com.example.restaurant.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailControllerTest {

    @InjectMocks
    private EmailController emailController;

    @Mock
    private JavaMailSender javaMailSender;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendOtp_Success() {
        // Prepare request body
        Map<String, String> request = new HashMap<>();
        request.put("email", "test@example.com");
        ResponseEntity<String> response = emailController.sendOtp(request);
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("OTP sent to your email");
        System.out.println("testSendOtp_Success: " + (response.getStatusCode() == HttpStatus.OK ? "Passed" : "Failed"));
    }

    @Test
    public void testVerifyOtp_Success() {
        Map<String, String> otpStorage = new HashMap<>();
        otpStorage.put("test@example.com", "123456");
        emailController.otpStorage = otpStorage;
        Map<String, String> request = new HashMap<>();
        request.put("email", "test@example.com");
        request.put("otp", "123456");

        ResponseEntity<String> response = emailController.verifyOtp(request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("OTP verified");
        System.out.println("testVerifyOtp_Success: "
                + (response.getStatusCode() == HttpStatus.OK && "OTP verified".equals(response.getBody()) ? "Passed"
                        : "Failed"));
    }

    @Test
    public void testVerifyOtp_Failure() {
        Map<String, String> otpStorage = new HashMap<>();
        otpStorage.put("test@example.com", "123456");
        emailController.otpStorage = otpStorage;

        Map<String, String> request = new HashMap<>();
        request.put("email", "test@example.com");
        request.put("otp", "654321");
        ResponseEntity<String> response = emailController.verifyOtp(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isEqualTo("Invalid OTP");
        System.out.println("testVerifyOtp_Failure: "
                + (response.getStatusCode() == HttpStatus.UNAUTHORIZED && "Invalid OTP".equals(response.getBody())
                        ? "Passed"
                        : "Failed"));
    }

    @Test
    public void testSendOtp_BadRequest() {
        Map<String, String> request = new HashMap<>();
        ResponseEntity<String> response = emailController.sendOtp(request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Email is required");
        System.out.println("testSendOtp_BadRequest: "
                + (response.getStatusCode() == HttpStatus.BAD_REQUEST && "Email is required".equals(response.getBody())
                        ? "Passed"
                        : "Failed"));
    }
}
