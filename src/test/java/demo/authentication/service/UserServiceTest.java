package demo.authentication.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import demo.authentication.entity.User;
import demo.authentication.repository.UserRepository;
import demo.authentication.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User user;

    @BeforeEach
    void setUp(){
        // GIVEN
        user = new User();
        user.setCreationDate(LocalDate.now());
        user.setEmail("oandres@demo.ec");
        user.setPwd("1234");
        user.setUsername("oandres");
    }

    @Test
    void registerUser_Success_WhenUserDoesNotExist(){
        
        // WHEN 
        // User does not exist
        when(userRepository.findById("oandres")).thenReturn(Optional.empty());
        // Encoded pwd
        when(passwordEncoder.encode("1234")).thenReturn("encodedPwd");
        // Save
        when(userRepository.save(any(User.class))).thenReturn(user);
        // Act
        User savedUser = userServiceImpl.registerUser(user);

        //THEN
        assertNotNull(savedUser);
        assertEquals("oandres", savedUser.getUsername());
        assertEquals("encodedPwd", savedUser.getPwd());
        assertEquals(user, savedUser);

        // Interactions
        verify(userRepository, times(1)).findById("oandres");
        verify(passwordEncoder, times(1)).encode("1234");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void registerUser_ThrowsException_WhenUserExist(){
        
        // WHEN
        User existingUser = new User();
        existingUser.setUsername("oandres");

        when(userRepository.findById("oandres")).thenReturn(Optional.of(existingUser));
        
        // Act
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class, 
            () ->  userServiceImpl.registerUser(user));

        // THEN
        // Assert exception details
        assertEquals(HttpStatus.CONFLICT.value(), exception.getStatusCode().value());
        assertTrue(exception.getMessage().contains("User oandres already exists"));

        // Verify interactions
        verify(userRepository, times(1)).findById("oandres");
        verify(passwordEncoder, never()).encode("1234");
        verify(userRepository, never()).save(any(User.class));
    }

}
