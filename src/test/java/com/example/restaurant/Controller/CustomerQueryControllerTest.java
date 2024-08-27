package com.example.restaurant.Controller;

import com.example.restaurant.Model.CustomerQuery;
import com.example.restaurant.Service.CustomerQueryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerQueryService service;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomerQuery testQuery;

    @BeforeEach
    public void setup() {
        testQuery = new CustomerQuery();
        testQuery.setId(1L);
        testQuery.setQuery_subject("Test query");
        testQuery.setQuery_message("Test message");
        testQuery.setStatus("Pending");
    }

    @Test
    public void testGetAllQueries() throws Exception {
        when(service.getAllQueries()).thenReturn(List.of(testQuery));

        mockMvc.perform(get("/query"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].query_subject", equalTo("Test query"))) // Adjusted field name
                .andExpect(jsonPath("$[0].query_message", equalTo("Test message"))) // Adjusted field name
                .andExpect(jsonPath("$[0].status", equalTo("Pending")));
    }

    @Test
    public void testGetQuery() throws Exception {
        when(service.getQueryById(1L)).thenReturn(Optional.of(testQuery));

        mockMvc.perform(get("/query/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.query_subject", equalTo("Test query"))) // Adjusted field name
                .andExpect(jsonPath("$.query_message", equalTo("Test message"))) // Adjusted field name
                .andExpect(jsonPath("$.status", equalTo("Pending")));
    }

    @Test
    public void testAddQuery() throws Exception {
        when(service.addQuery(any(CustomerQuery.class))).thenReturn(testQuery);

        mockMvc.perform(post("/query/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testQuery)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.query_subject", equalTo("Test query"))) // Adjusted field name
                .andExpect(jsonPath("$.query_message", equalTo("Test message"))) // Adjusted field name
                .andExpect(jsonPath("$.status", equalTo("Pending")));
    }

    @Test
    public void testDeleteQuery() throws Exception {
        when(service.deleteQuery(1L)).thenReturn(true);

        mockMvc.perform(delete("/query/remove/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Query removed successfully"));
    }

    @Test
    public void testUpdateQueryStatus() throws Exception {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("status", "Resolved");

        CustomerQuery updatedQuery = new CustomerQuery();
        updatedQuery.setId(1L);
        updatedQuery.setQuery_subject("Test query");
        updatedQuery.setQuery_message("Test message");
        updatedQuery.setStatus("Resolved");

        when(service.updateQueryStatus(eq(1L), eq("Resolved"))).thenReturn(updatedQuery);

        mockMvc.perform(put("/query/updateStatus/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo("Resolved")));
    }
}
