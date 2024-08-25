package com.example.restaurant.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restaurant.Model.Order;
import com.example.restaurant.Repository.OrderRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository repository;

    public List<Order> getAllOrders() {
        return repository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return repository.findById(id);
    }

    public Order addOrder(Order order) {
        return repository.save(order);
    }

    public boolean deleteOrder(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Order> getOrdersByDeliveryId(Long deliveryId) {
        return repository.findByDeliveryId(deliveryId);
    }
}
