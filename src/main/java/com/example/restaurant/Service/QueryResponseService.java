package com.example.restaurant.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restaurant.Model.QueryResponse;
import com.example.restaurant.Repository.QueryReponseRepository;

@Service
public class QueryResponseService {
    @Autowired
    private QueryReponseRepository repository;

    public List<QueryResponse> getAllResponses() {
        return repository.findAll();
    }

    public Optional<QueryResponse> getResponseById(Long id) {
        return repository.findById(id);
    }

    public QueryResponse addResponse(QueryResponse response) {
        return repository.save(response);
    }

    public boolean deleteResponse(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
