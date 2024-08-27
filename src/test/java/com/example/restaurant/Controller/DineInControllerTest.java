package com.example.restaurant.Controller;

import com.example.restaurant.Model.DineInReservation;
import com.example.restaurant.Model.User;
import com.example.restaurant.Service.DineInService;
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
public class DineInControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DineInService service;

    @Autowired
    private ObjectMapper objectMapper;

    private DineInReservation testReservation;
    private User testUser;

    @BeforeEach
    public void setup() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("john_doe");

        testReservation = new DineInReservation();
        testReservation.setId(1L);
        testReservation.setReservation_code("RES12345");
        testReservation.setUser(testUser);
        testReservation.setReservation_date_time(Timestamp.valueOf("2024-08-27 19:00:00"));
        testReservation.setNum_guests(4);
        testReservation.setSpecial_requests("Window seat");
        testReservation.setStatus("Confirmed");
    }

    @Test
    public void testGetAllReservations() throws Exception {
        when(service.getAllReservations()).thenReturn(List.of(testReservation));

        mockMvc.perform(get("/dinein"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].reservation_code", equalTo("RES12345")))
                .andExpect(jsonPath("$[0].num_guests", equalTo(4)));
    }

    @Test
    public void testGetReservation() throws Exception {
        when(service.getReservationById(1L)).thenReturn(Optional.of(testReservation));

        mockMvc.perform(get("/dinein/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.reservation_code", equalTo("RES12345")))
                .andExpect(jsonPath("$.num_guests", equalTo(4)));
    }

    @Test
    public void testAddReservation() throws Exception {
        when(service.addReservation(any(DineInReservation.class))).thenReturn(testReservation);

        mockMvc.perform(post("/dinein/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testReservation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reservation_code", equalTo("RES12345")))
                .andExpect(jsonPath("$.num_guests", equalTo(4)));
    }

    @Test
    public void testDeleteReservation() throws Exception {
        when(service.deleteReservation(1L)).thenReturn(true);

        mockMvc.perform(delete("/dinein/remove/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Reservation removed successfully"));
    }
}
