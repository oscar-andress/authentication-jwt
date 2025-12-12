package demo.authentication.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.authentication.dto.request.UserLoginRequest;
import demo.authentication.dto.response.UserLoginResponse;
import demo.authentication.mapper.UserMapper;
import demo.authentication.service.AuthService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/v1/auth")
@CrossOrigin

public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    AuthController(AuthService authService, 
                   UserMapper userMapper){
        this.authService = authService;
        this.userMapper = userMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> postLogin(@RequestBody UserLoginRequest request) {
        UserLoginResponse userLoginResponse = userMapper.toResponseLogin(authService.login(request));
        return new ResponseEntity<>(userLoginResponse, HttpStatus.OK);
    }
    
}
