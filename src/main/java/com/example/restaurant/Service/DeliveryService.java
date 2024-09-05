package com.example.restaurant.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.restaurant.Model.Delivery;
import com.example.restaurant.Repository.DeliveryRepository;

@Service
public class DeliveryService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private DeliveryRepository repository;

    public List<Delivery> getAllReservations() {
        return repository.findAll();
    }

    public Optional<Delivery> getReservationById(Long id) {
        return repository.findById(id);
    }

    public Delivery addItem(Delivery reservation) {
        return repository.save(reservation);
    }

    public boolean deleteItem(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Delivery> getDeliverynByUser(Long userId) {
        return repository.getAllDeliveryUser(userId);
    }

    public boolean cancelDelivery(Long deliveryId) {
        Optional<Delivery> delivery = repository.findById(deliveryId);
        if (delivery.isPresent()) {
            Delivery deliveryOrder = delivery.get();
            deliveryOrder.setStatus("Canceled");
            repository.save(deliveryOrder);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteDelivery(Long deliveryId) {
        if (repository.existsById(deliveryId)) {
            orderService.deleteOrdersByDeliveryId(deliveryId);
            paymentService.deletePaymentsByDeliveryId(deliveryId);
            repository.deleteById(deliveryId);
            return true;
        }
        return false;
    }

    public List<Delivery> geDeliveryByDateRange(Timestamp startDate, Timestamp endDate) {
        return repository.findByCreatedAtBetween(startDate, endDate);
    }
}
