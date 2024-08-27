package com.example.restaurant.Controller;

import com.example.restaurant.Model.Payment;
import com.example.restaurant.Service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Payment payment;

    @BeforeEach
    public void setup() {
        payment = new Payment(1L, null, BigDecimal.valueOf(100.00), new Timestamp(System.currentTimeMillis()));
    }

    @Test
    public void testGetAllPayments() throws Exception {
        List<Payment> payments = new ArrayList<>();
        payments.add(payment);
        when(paymentService.getAllPayments()).thenReturn(payments);

        mockMvc.perform(get("/payment")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(100.00));
    }

    @Test
    public void testGetPayment() throws Exception {
        when(paymentService.getPaymentById(anyLong())).thenReturn(Optional.of(payment));

        mockMvc.perform(get("/payment/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(100.00));
    }

    @Test
    public void testGetPaymentNotFound() throws Exception {
        when(paymentService.getPaymentById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/payment/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddPayment() throws Exception {
        Payment newPayment = new Payment(2L, null, BigDecimal.valueOf(200.00),
                new Timestamp(System.currentTimeMillis()));
        when(paymentService.addItem(any(Payment.class))).thenReturn(newPayment);

        mockMvc.perform(post("/payment/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPayment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(200.00));
    }

    @Test
    public void testDeletePayment() throws Exception {
        when(paymentService.deletePayment(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/payment/remove/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment removed successfully"));
    }

    @Test
    public void testDeletePaymentNotFound() throws Exception {
        when(paymentService.deletePayment(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/payment/remove/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Payment not found"));
    }

    @Test
    public void testGetPaymentByDeliveryId() throws Exception {
        when(paymentService.getPaymentByDeliveryId(anyLong())).thenReturn(payment);

        mockMvc.perform(get("/payment/getbydelivery/{deliveryId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(100.00));
    }
}
