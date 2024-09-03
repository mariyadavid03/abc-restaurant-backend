package com.example.restaurant.Service;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

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
        mockUser.setName("John Doe");
        mockUser.setEmail("newemail@email.com");
        mockUser.setMobileNo("+94777777777");
        mockUser.setId(1L);
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
        assertFalse(result);
    }

    @Test
    public void testUpdateUser_Sucess() {
        User userDetails = new User();
        userDetails.setName("Updated User");
        userDetails.setEmail("updateduser@example.com");
        userDetails.setMobileNo("0987654321");

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        User updatedUser = userService.updateUser(1L, userDetails);
        verify(userRepository, times(1)).save(mockUser);
        assertTrue(updatedUser.getName().equals("Updated User"));
        assertTrue(updatedUser.getEmail().equals("updateduser@example.com"));
        assertTrue(updatedUser.getMobileNo().equals("0987654321"));
        System.out.println("Assertions passed: User details were updated correctly");
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        User userDetails = new User();
        userDetails.setName("Updated User");
        userDetails.setEmail("updateduser@example.com");
        userDetails.setMobileNo("0987654321");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(1L, userDetails);
        });

        System.out.println("Exception message: " + exception.getMessage());
        assertTrue(exception.getMessage().contains("User not found with id 1"));
        System.out.println("Test passed: UserNotFound exception was thrown correctly.");
    }
}