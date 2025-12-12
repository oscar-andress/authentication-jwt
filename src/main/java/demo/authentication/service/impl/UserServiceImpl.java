package demo.authentication.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public User updatePwd(User user) {

        User updateUser = userRepository.findById(user.getUsername())
        .orElseThrow( () ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "User "+ user.getUsername()+" not found")
        );

        String encodedPwd = passwordEncoder.encode(user.getPwd());
        if(passwordEncoder.matches(user.getPwd(), updateUser.getPwd())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "New password is the same as the old one");
        }

        updateUser.setPwd(encodedPwd);
        return userRepository.save(updateUser);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAllByOrderByUsernameAsc();
    }
    
}
