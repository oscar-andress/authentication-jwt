package demo.authentication.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import demo.authentication.dto.request.UserLoginRequest;
import demo.authentication.entity.User;
import demo.authentication.repository.UserRepository;
import demo.authentication.service.AuthService;
import demo.authentication.service.JwtService;

@Service
public class AuthServieImpl implements AuthService{

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final JwtService jwtService;
    
    public AuthServieImpl(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User login(UserLoginRequest request) {

        // Found user
        User userFound = userRepository.findById(request.getUsername())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        // Check pwd
        if(!passwordEncoder.matches(request.getPwd(), userFound.getPwd())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Invalida credentials");
        }
        
        // Generate token 
        String jwt = jwtService.generateToken(request.getUsername());
        userFound.setJwt(jwt);
        return userRepository.save(userFound);
    }
    
}
