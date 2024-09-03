package com.example.restaurant.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.restaurant.Model.Delivery;
import com.example.restaurant.Model.Payment;
import com.example.restaurant.Repository.PaymentRepository;

public class PayementServiceTest {

    @Mock
    private PaymentRepository repository;

    @InjectMocks
    private PaymentService paymentService;

    private Payment mockPayment;
    private Delivery mockDelivery;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockDelivery = new Delivery();
        mockDelivery.setId(1L);
        mockDelivery.setStatus("Pending");

        mockPayment = new Payment();
        mockPayment.setId(1L);
        mockPayment.setDelivery(mockDelivery);
        mockPayment.setAmount(new BigDecimal("1234.00"));
    }

    @Test
    public void testAddPayment() {
        when(repository.save(mockPayment)).thenReturn(mockPayment);
        Payment result = paymentService.addItem(mockPayment);

        verify(repository, times(1)).save(mockPayment);
        assertEquals(mockPayment.getId(), result.getId());
        assertEquals(mockPayment.getAmount(), result.getAmount());

        System.out.println("Add Payment test passed");
    }

}
