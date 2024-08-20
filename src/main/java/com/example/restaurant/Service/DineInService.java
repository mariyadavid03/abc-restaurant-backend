package com.example.restaurant.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restaurant.Model.DineInReservation;
import com.example.restaurant.Repository.DineInRepository;

@Service
public class DineInService {
    @Autowired
    private DineInRepository repository;

    public List<DineInReservation> getAllReservations() {
        return repository.findAll();
    }

    public Optional<DineInReservation> getReservationById(Long id) {
        return repository.findById(id);
    }

    public DineInReservation addReservation(DineInReservation reservation) {
        return repository.save(reservation);
    }

    public boolean deleteReservation(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

}
