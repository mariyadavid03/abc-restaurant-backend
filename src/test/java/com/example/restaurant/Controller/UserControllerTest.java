package com.example.restaurant.Controller;

import com.example.restaurant.Model.User;
import com.example.restaurant.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    public void setup() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setRole("USER");
    }

    @Test
    public void testGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(testUser));

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].username", equalTo("testuser")));
    }

    @Test
    public void testGetUser() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", equalTo("testuser")));
    }

    @Test
    public void testAddUser() throws Exception {
        when(userService.addUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/user/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", equalTo("testuser")));
    }

    @Test
    public void testDeleteUser() throws Exception {
        when(userService.deleteUser(1L)).thenReturn(Optional.of(testUser));

        mockMvc.perform(delete("/user/remove/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User removed successfully"));
    }

    @Test
    public void testLogin() throws Exception {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "testuser");
        loginRequest.put("password", "password");
        loginRequest.put("role", "USER");

        when(userService.authenticateUser(eq("testuser"), eq("password"))).thenReturn(true);
        when(userService.findByUsername("testuser")).thenReturn(testUser);

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", equalTo("Login successful")))
                .andExpect(jsonPath("$.role", equalTo("USER")));
    }

    @Test
    public void testSignup() throws Exception {
        doNothing().when(userService).createUser(any(User.class)); // Ensure createUser does not return a value

        mockMvc.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(content().string("Signup successful"));
    }

    @Test
    public void testGetUserIdByUsername() throws Exception {
        when(userService.getUserIdByUsername("testuser")).thenReturn(1L);

        mockMvc.perform(get("/user/getByUsername?username=testuser"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    public void testUpdateAccount() throws Exception {
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("updateduser");
        updatedUser.setPassword("newpassword");
        updatedUser.setRole("USER");

        when(userService.updateUser(1L, updatedUser)).thenReturn(updatedUser);

        mockMvc.perform(put("/user/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", equalTo("updateduser")));
    }
}
