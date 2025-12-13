package demo.authentication.service;

import java.util.List;

import demo.authentication.dto.request.UserUpdateRequest;
import demo.authentication.entity.User;

public interface UserService {
    List<User> getUsers();
    User updatePwd(UserUpdateRequest request);
    User registerUser(User user);
    User saveUser(User user);
}
