package com.example.restaurant.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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

    @Test
    public void testSendResponse() {
        Map<String, String> request = new HashMap<>();
        request.put("email", "customer@example.com");
        request.put("response", "Thank you for your query. Here is the response.");

        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
        ResponseEntity<String> responseEntity = emailController.sendResponse(request);

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
        assert (responseEntity.getStatusCode() == HttpStatus.OK);
        assert (responseEntity.getBody().equals("Response Sent"));
        System.out.println("Query response email sent successfully");
    }

    @Test
    public void testSendReservationEmail() {
        Map<String, String> request = new HashMap<>();
        request.put("email", "customer@example.com");
        request.put("reservation_code", "RES123");
        request.put("reservation_date_time", "2024-09-02 19:00");
        request.put("num_guests", "4");

        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
        ResponseEntity<String> responseEntity = emailController.sendReservationEmail(request);

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
        assert (responseEntity.getStatusCode() == HttpStatus.OK);
        assert (responseEntity.getBody().equals("Reservation Details Sent"));
        System.out.println("Reservation confirmation email sent successfully");
    }

    @Test
    public void testSendPaymentEmail() {
        Map<String, String> request = new HashMap<>();
        request.put("email", "customer@example.com");
        request.put("code", "DEL123");
        request.put("amount", "5000");
        request.put("createdAt", "2024-09-02 10:00");

        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
        ResponseEntity<String> responseEntity = emailController.sendPaymentEmail(request);

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
        assert (responseEntity.getStatusCode() == HttpStatus.OK);
        assert (responseEntity.getBody().equals("Delivery & Payment Details Sent"));
        System.out.println("Delivery confirmation email sent successfully");
    }
}
