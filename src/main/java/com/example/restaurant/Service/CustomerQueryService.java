package com.example.restaurant.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restaurant.Model.CustomerQuery;
import com.example.restaurant.Repository.CustomerQueryRepository;

@Service

public class CustomerQueryService {
    @Autowired
    private CustomerQueryRepository repository;

    public List<CustomerQuery> getAllQueries() {
        return repository.findAll();
    }

    public Optional<CustomerQuery> getQueryById(Long id) {
        return repository.findById(id);
    }

    public CustomerQuery addQuery(CustomerQuery query) {
        return repository.save(query);
    }

    public boolean deleteQuery(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

}
