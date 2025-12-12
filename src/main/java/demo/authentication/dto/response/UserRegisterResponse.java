package demo.authentication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserRegisterResponse {
    private String username;
    private String email;
    private String rol;
    private String jwt;
}
