package demo.authentication.service;

import java.util.List;

import demo.authentication.entity.User;

public interface UserService {
    List<User> getUsers();
    User updatePwd(User user);
    User registerUser(User user);
    User saveUser(User user);
}
