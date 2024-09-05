package com.example.restaurant.Controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restaurant.Model.DineInReservation;
import com.example.restaurant.Service.DineInService;

@RestController
@RequestMapping("/dinein")
public class DineInController {
    @Autowired
    private DineInService service;

    @GetMapping
    public ResponseEntity<List<DineInReservation>> getAllReservations() {
        return new ResponseEntity<>(service.getAllReservations(), HttpStatus.OK);
    }

    @GetMapping("/getReservationByUser/{id}")
    public ResponseEntity<List<DineInReservation>> getReservationByUser(@PathVariable Long id) {
        List<DineInReservation> reservations = service.getReservationByUser(id);
        if (reservations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @PutMapping("/cancelReservation/{id}")
    public ResponseEntity<String> cancelReservation(@PathVariable Long id) {
        boolean isCanceled = service.cancelReservation(id);
        if (isCanceled) {
            return new ResponseEntity<>("Reservation canceled successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Reservation not found or could not be canceled", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DineInReservation> getReservation(@PathVariable Long id) {
        Optional<DineInReservation> optional = service.getReservationById(id);
        return optional.map(i -> new ResponseEntity<>(i, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/add")
    public ResponseEntity<DineInReservation> addReservation(@RequestBody DineInReservation reservation) {
        DineInReservation newReservation = service.addReservation(reservation);
        return new ResponseEntity<>(newReservation, HttpStatus.CREATED);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Long id) {
        boolean isDeleted = service.deleteReservation(id);
        if (isDeleted) {
            return new ResponseEntity<>("Reservation removed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Reservation not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<List<DineInReservation>> getReservationsByDateRange(
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr) {

        try {
            Timestamp startDate = Timestamp.valueOf(startDateStr + " 00:00:00");
            Timestamp endDate = Timestamp.valueOf(endDateStr + " 23:59:59");
            List<DineInReservation> reservations = service.getReservationByDateRange(startDate, endDate);
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
