package com.example.restaurant.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restaurant.Model.DeliveryReservation;
import com.example.restaurant.Repository.DeliveryRepository;

@Service
public class DeliveryService {
    @Autowired
    private DeliveryRepository repository;

    public List<DeliveryReservation> getAllReservations() {
        return repository.findAll();
    }

    public Optional<DeliveryReservation> getReservationById(Long id) {
        return repository.findById(id);
    }

    public DeliveryReservation addItem(DeliveryReservation reservation) {
        return repository.save(reservation);
    }

    public boolean deleteItem(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
