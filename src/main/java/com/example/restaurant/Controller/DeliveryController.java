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

    @DeleteMapping("/removeList/{deliveryId}")
    public ResponseEntity<String> deleteDelivery(@PathVariable Long deliveryId) {
        boolean isDeleted = service.deleteDelivery(deliveryId);
        if (isDeleted) {
            return new ResponseEntity<>("Delivery and associated records removed successfully", HttpStatus.OK);
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

    @GetMapping("/filter")
    public ResponseEntity<List<Delivery>> getDeliveryByDateRange(
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr) {

        try {
            Timestamp startDate = Timestamp.valueOf(startDateStr + " 00:00:00");
            Timestamp endDate = Timestamp.valueOf(endDateStr + " 23:59:59");
            List<Delivery> deliveries = service.geDeliveryByDateRange(startDate, endDate);
            return new ResponseEntity<>(deliveries, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
