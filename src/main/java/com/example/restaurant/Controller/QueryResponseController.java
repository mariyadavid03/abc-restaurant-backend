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

import com.example.restaurant.Model.QueryResponse;
import com.example.restaurant.Service.QueryResponseService;

@RestController
@RequestMapping("/response")

public class QueryResponseController {
    @Autowired
    private QueryResponseService service;

    @GetMapping
    public ResponseEntity<List<QueryResponse>> gettAllResponses() {
        return new ResponseEntity<>(service.getAllResponses(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QueryResponse> getResponse(@PathVariable Long id) {
        Optional<QueryResponse> optional = service.getResponseById(id);
        return optional.map(i -> new ResponseEntity<>(i, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/query/{queryId}")
    public ResponseEntity<QueryResponse> getResponseByQueryId(@PathVariable("queryId") Long queryId) {
        QueryResponse response = service.getResponseByQueryId(queryId);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<QueryResponse> addResponse(@RequestBody QueryResponse response) {
        QueryResponse newResponse = service.addResponse(response);
        return new ResponseEntity<>(newResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deleteResponse(@PathVariable Long id) {
        boolean isDeleted = service.deleteResponse(id);
        if (isDeleted) {
            return new ResponseEntity<>("Response removed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Response not found", HttpStatus.NOT_FOUND);
        }
    }
}
