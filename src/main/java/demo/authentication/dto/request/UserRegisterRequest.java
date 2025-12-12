package demo.authentication.dto.request;

import demo.authentication.enumeration.RolEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserRegisterRequest {    
    private String username;
    private String email;
    private String pwd;
    private RolEnum rol; // "ADMIN", "USER"
}
