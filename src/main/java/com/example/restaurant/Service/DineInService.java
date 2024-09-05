package com.example.restaurant.Service;

import java.sql.Timestamp;
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

    public List<DineInReservation> getReservationByUser(Long userId) {
        return repository.getAllReservationUser(userId);
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

    public boolean cancelReservation(Long reservationId) {
        Optional<DineInReservation> reservation = repository.findById(reservationId);
        if (reservation.isPresent()) {
            DineInReservation dineInReservation = reservation.get();
            dineInReservation.setStatus("Canceled");
            repository.save(dineInReservation);
            return true;
        }
        return false;
    }

    public List<DineInReservation> getReservationByDateRange(Timestamp startDate, Timestamp endDate) {
        return repository.findByReservationDateTimeBetween(startDate, endDate);
    }

}
