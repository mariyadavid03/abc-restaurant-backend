package com.example.restaurant.Controller;

import com.example.restaurant.Model.DeliveryReservation;
import com.example.restaurant.Model.User;
import com.example.restaurant.Service.DeliveryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DeliveryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryService service;

    @Autowired
    private ObjectMapper objectMapper;

    private DeliveryReservation testReservation;
    private User testUser;

    @BeforeEach
    public void setup() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("john_doe");

        testReservation = new DeliveryReservation();
        testReservation.setId(1L);
        testReservation.setUser(testUser);
        testReservation.setDelivery_address("123 Main St");
        testReservation.setDelivery_code("DEL12345");
        testReservation.setDelivery_date_time(Timestamp.valueOf("2024-08-27 18:30:00"));
        testReservation.setSpecial_instructions("Leave at the door");
        testReservation.setStatus("Pending");
    }

    @Test
    public void testGetAllReservations() throws Exception {
        when(service.getAllReservations()).thenReturn(List.of(testReservation));

        mockMvc.perform(get("/delivery"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].delivery_code", equalTo("DEL12345")))
                .andExpect(jsonPath("$[0].delivery_address", equalTo("123 Main St")))
                .andExpect(jsonPath("$[0].status", equalTo("Pending")));
    }

    @Test
    public void testGetReservation() throws Exception {
        when(service.getReservationById(1L)).thenReturn(Optional.of(testReservation));

        mockMvc.perform(get("/delivery/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.delivery_code", equalTo("DEL12345")))
                .andExpect(jsonPath("$.delivery_address", equalTo("123 Main St")))
                .andExpect(jsonPath("$.status", equalTo("Pending")));
    }

    @Test
    public void testAddReservation() throws Exception {
        when(service.addItem(any(DeliveryReservation.class))).thenReturn(testReservation);

        mockMvc.perform(post("/delivery/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testReservation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.delivery_code", equalTo("DEL12345")))
                .andExpect(jsonPath("$.delivery_address", equalTo("123 Main St")))
                .andExpect(jsonPath("$.status", equalTo("Pending")));
    }

    @Test
    public void testDeleteReservation() throws Exception {
        when(service.deleteItem(1L)).thenReturn(true);

        mockMvc.perform(delete("/delivery/remove/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Reservation removed successfully"));
    }

    @Test
    public void testAddDelivery() throws Exception {
        when(service.addItem(any(DeliveryReservation.class))).thenReturn(testReservation);

        mockMvc.perform(post("/delivery/delivery/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testReservation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.delivery_code", equalTo("DEL12345")))
                .andExpect(jsonPath("$.delivery_address", equalTo("123 Main St")))
                .andExpect(jsonPath("$.status", equalTo("Pending")));
    }
}
