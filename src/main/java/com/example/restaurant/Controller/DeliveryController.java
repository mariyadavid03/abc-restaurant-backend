package com.example.restaurant.Controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.example.restaurant.Model.Delivery;
import com.example.restaurant.Service.DeliveryService;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {
    @Autowired
    private DeliveryService service;

    @GetMapping
    public ResponseEntity<List<Delivery>> getAllReservations() {
        return new ResponseEntity<>(service.getAllReservations(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Delivery> getReservation(@PathVariable Long id) {
        Optional<Delivery> optional = service.getReservationById(id);
        return optional.map(i -> new ResponseEntity<>(i, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/add")
    public ResponseEntity<Delivery> addReservation(@RequestBody Delivery reservation) {
        Delivery newReservation = service.addItem(reservation);
        return new ResponseEntity<>(newReservation, HttpStatus.CREATED);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Long id) {
        boolean isDeleted = service.deleteItem(id);
        if (isDeleted) {
            return new ResponseEntity<>("Delivery removed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Delivery not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/delivery/add")
    public ResponseEntity<Delivery> addDelivery(@RequestBody Delivery delivery) {
        Delivery savedDelivery = service.addItem(delivery);

        return ResponseEntity.ok(savedDelivery);
    }

    @GetMapping("/getDeliverynByUser/{id}")
    public ResponseEntity<List<Delivery>> getDeliverynByUser(@PathVariable Long id) {
        List<Delivery> delivery = service.getDeliverynByUser(id);
        if (delivery.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(delivery, HttpStatus.OK);
    }

    @PutMapping("/cancelDelivery/{id}")
    public ResponseEntity<String> cancelDelivery(@PathVariable Long id) {
        boolean isCanceled = service.cancelDelivery(id);
        if (isCanceled) {
            return new ResponseEntity<>("Delivery canceled successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Delivery not found or could not be canceled", HttpStatus.NOT_FOUND);
        }
    }
}
