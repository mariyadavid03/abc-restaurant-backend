package com.example.restaurant.Service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.restaurant.Model.User;
import com.example.restaurant.Repository.UserRepository;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User mockUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = new User();
        mockUser.setUsername("testUser");
        mockUser.setPassword("encodedPassword");
    }

    @Test
    public void testAuthenticateUser_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(mockUser);
        when(passwordEncoder.matches("plainPassword", "encodedPassword")).thenReturn(true);

        boolean result = userService.authenticateUser("testUser", "plainPassword");
        System.out.println("Result of authentication sucess: " + result);

        assertTrue(result);
    }

    @Test
    public void testAuthenticateUser_Fail() {
        when(userRepository.findByUsername("testUser")).thenReturn(mockUser);
        when(passwordEncoder.matches("plainPassword", "encodedPassword")).thenReturn(true);

        boolean result = userService.authenticateUser("invalidUser", "invalidPassword");
        System.out.println("Result of authentication fail: " + result);
    }
}