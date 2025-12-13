package demo.authentication.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import demo.authentication.dto.request.UserUpdateRequest;
import demo.authentication.entity.User;
import demo.authentication.repository.UserRepository;
import demo.authentication.service.UserService;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    UserServiceImpl(UserRepository userRepository,
                    PasswordEncoder passwordEncoder
    ){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User registerUser(User user) {

        userRepository.findById(user.getUsername())
            .ifPresent(userFound ->{
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User "+ user.getUsername()+" already exists");
            });

        String encodedPwd = passwordEncoder.encode(user.getPwd());
        user.setPwd(encodedPwd);
        return userRepository.save(user);
    }

    @Override
    public User updatePwd(UserUpdateRequest request) {

        // Find user
        User updateUser = userRepository.findById(request.getUsername())
        .orElseThrow( () ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "User "+ request.getUsername()+" not found")
        );

        // Check current and new pwd
        String encodedPwd = passwordEncoder.encode(request.getPwd());
        if(passwordEncoder.matches(request.getPwd(), updateUser.getPwd())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "New password is the same as the old one");
        }

        // Change pwd
        updateUser.setPwd(encodedPwd);
        return userRepository.save(updateUser);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAllByOrderByUsernameAsc();
    }
    
}
