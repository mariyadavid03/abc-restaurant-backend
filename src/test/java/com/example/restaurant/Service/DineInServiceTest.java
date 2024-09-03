package com.example.restaurant.Service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.restaurant.Model.DineInReservation;
import com.example.restaurant.Repository.DineInRepository;

public class DineInServiceTest {

    @Mock
    private DineInRepository repository;

    @InjectMocks
    private DineInService dineInService;

    private DineInReservation mockReservation;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockReservation = new DineInReservation();
        mockReservation.setId(1L);
        mockReservation.setStatus("Reserved");
    }

    @Test
    public void testAddReservation() {
        when(repository.save(mockReservation)).thenReturn(mockReservation);
        DineInReservation result = dineInService.addReservation(mockReservation);

        verify(repository, times(1)).save(mockReservation);
        assertTrue(result.getId().equals(mockReservation.getId()));
        assertTrue(result.getStatus().equals(mockReservation.getStatus()));
        System.out.println("Add Reservation test passed");
    }

    @Test
    public void testDeleteReservation_Success() {
        when(repository.existsById(1L)).thenReturn(true);
        boolean result = dineInService.deleteReservation(1L);

        verify(repository, times(1)).deleteById(1L);
        assertTrue(result);
        System.out.println("Delete Reservation test passed");
    }

    @Test
    public void testDeleteReservation_Failure() {
        when(repository.existsById(1L)).thenReturn(false);
        boolean result = dineInService.deleteReservation(1L);

        verify(repository, times(0)).deleteById(1L);
        assertFalse(result);
        System.out.println("Delete Reservation Failure test passed");
    }

    @Test
    public void testCancelReservation_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(mockReservation));
        when(repository.save(mockReservation)).thenReturn(mockReservation);

        boolean result = dineInService.cancelReservation(1L);

        verify(repository, times(1)).save(mockReservation);
        assertTrue(result);
        assertTrue(mockReservation.getStatus().equals("Canceled"));
        System.out.println("Cancel Reservation test passed");
    }
}
