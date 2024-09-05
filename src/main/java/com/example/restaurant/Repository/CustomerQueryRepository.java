package com.example.restaurant.Repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restaurant.Model.CustomerQuery;

@Repository
public interface CustomerQueryRepository extends JpaRepository<CustomerQuery, Long> {

    List<CustomerQuery> findByCreatedAtBetween(Timestamp startDate, Timestamp endDate);
}
