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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restaurant.Model.Payment;
import com.example.restaurant.Service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentService service;

    @GetMapping
    public ResponseEntity<List<Payment>> gettAllPayments() {
        return new ResponseEntity<>(service.getAllPayments(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable Long id) {
        Optional<Payment> optional = service.getPaymentById(id);
        return optional.map(i -> new ResponseEntity<>(i, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/add")
    public ResponseEntity<Payment> addPayment(@RequestBody Payment payment) {
        Payment newPayment = service.addItem(payment);
        return new ResponseEntity<>(newPayment, HttpStatus.CREATED);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deletePayment(@PathVariable Long id) {
        boolean isDeleted = service.deletePayment(id);
        if (isDeleted) {
            return new ResponseEntity<>("Payment removed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Payment not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getbydelivery/{deliveryId}")
    public Payment getPaymentByDeliveryId(@PathVariable Long deliveryId) {
        return service.getPaymentByDeliveryId(deliveryId);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Payment>> getPaymentsByDateRange(
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr) {

        try {
            Timestamp startDate = Timestamp.valueOf(startDateStr + " 00:00:00");
            Timestamp endDate = Timestamp.valueOf(endDateStr + " 23:59:59");
            List<Payment> payments = service.getPaymentsByDateRange(startDate, endDate);
            return new ResponseEntity<>(payments, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
