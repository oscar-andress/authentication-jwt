package demo.authentication.service.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import demo.authentication.dto.request.UserLoginRequest;
import demo.authentication.entity.User;
import demo.authentication.repository.UserRepository;
import demo.authentication.service.AuthService;

@SpringBootTest
@ActiveProfiles("test")
@Transactional

public class AuthServiceTest {
    
    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private User testUser;

    @Autowired
    public AuthServiceTest(AuthService authService, 
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder){
        this.authService = authService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();

        testUser = new User();
        testUser.setUsername("oandres");
        testUser.setPwd(passwordEncoder.encode("123"));
        testUser.setCreationDate(LocalDate.now());
        testUser.setEmail("oandres@example.com");
        testUser.setRol("ADMIN");

        userRepository.save(testUser);
    }

    @Test
    void login_Success_WhenUserExist(){
        // WHEN
        UserLoginRequest request = new UserLoginRequest();
        request.setUsername("oandres");
        request.setPwd("123");

        // ACT
        User logedUser = authService.login(request);

        // THEN
        assertNotNull(logedUser);
        assertNotNull(logedUser.getJwt());

        // Verify
        // User saved in DB
        User savedUser = userRepository.findById(request.getUsername()).orElseThrow();
        assertEquals(logedUser, savedUser);
    }

    @Test
    void login_ThrowsException_WhenUserNotExist(){
        // WHEN
        UserLoginRequest request = new UserLoginRequest();
        request.setUsername("aandres");
        request.setPwd("123");
        
        // ACT
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class, 
            () -> authService.login(request));
        
        // THEN
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getReason().contains("User not found"));
    }

    @Test
    void login_ThrowsException_WhenPasswordIncorrect(){
        // WHEN 
        UserLoginRequest request = new UserLoginRequest();
        request.setUsername("oandres");
        request.setPwd("wronPwd");

        // ACT
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class, 
            () ->authService.login(request));
        
        // THEN
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Invalid credentials"));
    }

        @Test
    void login_Success_PwdIsSavedWithBcrypted(){
        // WHEN
        UserLoginRequest request = new UserLoginRequest();
        request.setUsername("oandres");
        request.setPwd("123");

        // ACT
        User logedUser = authService.login(request);

        // THEN
        assertNotNull(logedUser);
        assertNotNull(logedUser.getJwt());

        // Verify
        // User saved in DB
        User savedUser = userRepository.findById(request.getUsername()).orElseThrow();
        assertEquals(logedUser, savedUser);
        assertTrue(savedUser.getPwd().startsWith("{bcrypt}"), "Password should be BCrypt encoded");
    }
}
