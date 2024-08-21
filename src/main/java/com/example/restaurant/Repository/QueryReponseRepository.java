package com.example.restaurant.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restaurant.Model.QueryResponse;

@Repository
public interface QueryReponseRepository extends JpaRepository<QueryResponse, Long> {
    QueryResponse findByQueryId(Long queryId);
}
