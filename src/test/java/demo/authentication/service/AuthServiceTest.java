package demo.authentication.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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

import demo.authentication.dto.request.UserLoginRequest;
import demo.authentication.entity.User;
import demo.authentication.repository.UserRepository;
import demo.authentication.service.impl.AuthServieImpl;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServieImpl authServieImpl;

    private UserLoginRequest request;
    private User user;

    @BeforeEach
    void setUp(){
        // GIVEN
        request = new UserLoginRequest();
        request.setUsername("oandres");
        request.setPwd("1234");

        // GIVEN
        user = new User();
        user.setUsername("ovega");
        user.setCreationDate(LocalDate.now());
        user.setEmail("oandres@example.com");
        user.setPwd("abc");
        user.setRol("ADMIN");
    }

    @Test
    void login_Success_WhenUserExist(){
        // WHEN
        when(userRepository.findById(request.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPwd(), user.getPwd())).thenReturn(true);
        when(jwtService.generateToken(request.getUsername())).thenReturn("abc");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // ACT
        User userLoged = authServieImpl.login(request);

        // THEN
        assertNotNull(userLoged);
        assertNotNull(userLoged.getJwt());
        verify(userRepository, times(1)).findById(request.getUsername());
        verify(passwordEncoder, times(1)).matches(request.getPwd(), user.getPwd());
        verify(jwtService, times(1)).generateToken(request.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void login_ThrowException_WhenUserNotExist(){
        // WHEN
        when(userRepository.findById(request.getUsername())).thenReturn(Optional.empty());

        // ACT
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class, 
            () -> authServieImpl.login(request));
        
        // THEN
        assertEquals(exception.getStatusCode(), HttpStatus.NOT_FOUND);
        assertTrue(exception.getMessage().contains("User not found"));
        verify(userRepository, times(1)).findById(request.getUsername());
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(jwtService);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_ThrowsException_WhenPasswordIncorrect(){
        // WHEN
        when(userRepository.findById(request.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPwd(), user.getPwd())).thenReturn(false);

        // ACT
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class, 
            () -> authServieImpl.login(request));

        // THEN
        assertEquals(exception.getStatusCode(), HttpStatus.CONFLICT);
        assertTrue(exception.getMessage().contains("Invalid credentials"));
        verify(userRepository, times(1)).findById(request.getUsername());
        verify(passwordEncoder, times(1)).matches(request.getPwd(), user.getPwd());
        verifyNoInteractions(jwtService);
        verify(userRepository, never()).save(any(User.class));
    }
}