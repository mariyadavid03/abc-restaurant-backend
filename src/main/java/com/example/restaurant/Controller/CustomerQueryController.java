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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restaurant.Model.CustomerQuery;
import com.example.restaurant.Service.CustomerQueryService;

@RestController
@RequestMapping("/query")
public class CustomerQueryController {
    @Autowired
    private CustomerQueryService service;

    @GetMapping
    public ResponseEntity<List<CustomerQuery>> gettAllQueries() {
        return new ResponseEntity<>(service.getAllQueries(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerQuery> getQuery(@PathVariable Long id) {
        Optional<CustomerQuery> optional = service.getQueryById(id);
        return optional.map(i -> new ResponseEntity<>(i, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/add")
    public ResponseEntity<CustomerQuery> addQuery(@RequestBody CustomerQuery query) {
        CustomerQuery newQuery = service.addQuery(query);
        return new ResponseEntity<>(newQuery, HttpStatus.CREATED);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deleteQuery(@PathVariable Long id) {
        boolean isDeleted = service.deleteQuery(id);
        if (isDeleted) {
            return new ResponseEntity<>("Query removed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Query not found", HttpStatus.NOT_FOUND);
        }
    }
}
