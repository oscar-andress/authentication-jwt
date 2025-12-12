package demo.authentication.config;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import demo.authentication.entity.User;
import demo.authentication.repository.UserRepository;


@Service
public class MyUserDetailServiceImpl implements UserDetailsService{

    private final UserRepository userRepository;
    MyUserDetailServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRol()));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPwd(), authorities);
    }
}
