package demo.authentication.mapper;

import org.springframework.stereotype.Component;

import demo.authentication.dto.request.UserRegisterRequest;
import demo.authentication.dto.response.UserLoginResponse;
import demo.authentication.dto.response.UserRegisterResponse;
import demo.authentication.entity.User;

@Component
public class UserMapper {
    public User toEntity(UserRegisterRequest request){
        User user = new User();
        user.setUsername(request.getUsername());
        user.setRol(request.getRol().toString());
        user.setEmail(request.getEmail());
        user.setPwd(request.getPwd());
        return user;
    }

    public UserRegisterResponse toReponse(User user){
        UserRegisterResponse response = new UserRegisterResponse();
        response.setEmail(user.getEmail());
        response.setRol(user.getRol());
        response.setUsername(user.getUsername());
        response.setJwt(user.getJwt());
        return response;
    }

    public UserLoginResponse toResponseLogin(User user){
        UserLoginResponse response = new UserLoginResponse();
        response.setUsername(user.getUsername());
        response.setJwt(user.getJwt());
        return response;
    }
}
