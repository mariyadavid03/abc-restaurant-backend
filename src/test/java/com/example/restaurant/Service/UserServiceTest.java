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
    public void testCreateUser_WithStrongPassword() {
        // Arrange
        User userWithStrongPassword = new User();
        userWithStrongPassword.setUsername("newUser");
        userWithStrongPassword.setPassword("StrongP@ssw0rd!");
        userWithStrongPassword.setEmail("stronguser@example.com");

        // Act
        when(userRepository.findByEmail(userWithStrongPassword.getEmail())).thenReturn(null);
        when(userRepository.findByUsername(userWithStrongPassword.getUsername())).thenReturn(null);
        when(passwordEncoder.encode("StrongP@ssw0rd!")).thenReturn("encodedPassword");
        userService.createUser(userWithStrongPassword);

        // Assert
        verify(userRepository, times(1)).save(userWithStrongPassword);
        assertTrue(userWithStrongPassword.getPassword().equals("encodedPassword"));
        System.out.println("Test passed: User with a strong password was created successfully.");
    }

    @Test
    public void testCreateUser_WithWeakPassword() {
        // Arrange
        User userWithWeakPassword = new User();
        userWithWeakPassword.setUsername("newUser");
        userWithWeakPassword.setPassword("weakpass");
        userWithWeakPassword.setEmail("weakuser@example.com");

        // Act
        when(userRepository.findByEmail(userWithWeakPassword.getEmail())).thenReturn(null);
        when(userRepository.findByUsername(userWithWeakPassword.getUsername())).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(userWithWeakPassword);
        });

        // Assert
        assertTrue(exception.getMessage().contains("Password does not meet strength requirements"));
        verify(userRepository, times(0)).save(userWithWeakPassword);
        System.out.println("Test passed: Exception was thrown for weak password.");
    }

    @Test
    public void testAuthenticateUser_Success() {
        // Arrange
        when(userRepository.findByUsername("testUser")).thenReturn(mockUser);
        when(passwordEncoder.matches("plainPassword", "encodedPassword")).thenReturn(true);

        // Act
        boolean result = userService.authenticateUser("testUser", "plainPassword");
        System.out.println("Result of authentication sucess: " + result);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAuthenticateUser_Fail() {
        // Arrange
        when(userRepository.findByUsername("testUser")).thenReturn(mockUser);
        when(passwordEncoder.matches("plainPassword", "encodedPassword")).thenReturn(true);

        // Act
        boolean result = userService.authenticateUser("invalidUser", "invalidPassword");
        System.out.println("Result of authentication fail: " + result);

        // Assert
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