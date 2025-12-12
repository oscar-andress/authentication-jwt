package demo.authentication.service;

import demo.authentication.dto.request.UserLoginRequest;
import demo.authentication.entity.User;

public interface AuthService {
    User login(UserLoginRequest request);
}
