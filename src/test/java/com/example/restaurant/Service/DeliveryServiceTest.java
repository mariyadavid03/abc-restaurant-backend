package com.example.restaurant.Service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.restaurant.Model.Delivery;
import com.example.restaurant.Model.User;
import com.example.restaurant.Repository.DeliveryRepository;

public class DeliveryServiceTest {
    @Mock
    private DeliveryRepository repository;

    @InjectMocks
    private DeliveryService deliveryService;

    private Delivery mockDelivery;
    private User mockUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockDelivery = new Delivery();
        mockDelivery.setId(1L);
        mockDelivery.setUser(mockUser);
    }

    @Test
    public void testAddDelivery() {
        when(repository.save(mockDelivery)).thenReturn(mockDelivery);
        Delivery result = deliveryService.addItem(mockDelivery);

        verify(repository, times(1)).save(mockDelivery);
        assertTrue(result.getId().equals(mockDelivery.getId()));
        System.out.println("Add Delivery test passed");
    }

    @Test
    public void testDeleteDelivery_Sucess() {
        when(repository.existsById(1L)).thenReturn(true);
        boolean result = deliveryService.deleteItem(1L);
        verify(repository, times(1)).deleteById(1L);
        assertTrue(result);
        System.out.println("Delete Delivery Success test passed");

    }

    @Test
    public void testDeleteDelivery_Failure() {
        when(repository.existsById(1L)).thenReturn(false);
        boolean result = deliveryService.deleteItem(1L);

        verify(repository, times(0)).deleteById(1L);
        assertFalse(result);
        System.out.println("Delete Delivery Failure test passed");
    }
}
