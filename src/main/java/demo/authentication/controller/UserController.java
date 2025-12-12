package demo.authentication.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.authentication.dto.request.UserRegisterRequest;
import demo.authentication.dto.request.UserUpdateRequest;
import demo.authentication.dto.response.UserRegisterResponse;
import demo.authentication.dto.response.UserUpdateResponse;
import demo.authentication.entity.User;
import demo.authentication.mapper.UserMapper;
import demo.authentication.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@CrossOrigin
@RequestMapping("/v1/user")

public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    UserController(UserService userService,
                   UserMapper userMapper
    ){
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getUserAll() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }
    
    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> createUserRegister(@RequestBody UserRegisterRequest request) {
        User user = userMapper.toEntity(request);
        User registeredUser  = userService.registerUser(user);
        UserRegisterResponse response = userMapper.toReponse(registeredUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/update/pwd")
    public ResponseEntity<UserUpdateResponse> updateUserPwd(@RequestBody UserUpdateRequest request) {
        User updatedUser = userService.updatePwd(request);
        return new ResponseEntity<>( userMapper.toResponseUpdate(updatedUser), HttpStatus.OK);
    }
}
