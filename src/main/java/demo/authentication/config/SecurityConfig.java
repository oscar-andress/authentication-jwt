package demo.authentication.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import demo.authentication.filter.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter){
        this.jwtFilter = jwtFilter;
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrfConfig -> csrfConfig.disable())
                .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/v1/user/register", 
                                 "/v1/auth/login",
                                 "/actuator/health",
                                 "/actuator/health/**").permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint((request, response, authException) -> {
                    sendError(response, HttpStatus.UNAUTHORIZED, "Invalid token");
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    // This handles 403 when token is valid but lacks permissions
                    sendError(response, HttpStatus.FORBIDDEN, "Insufficient permissions");
                })
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );
        
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    private void sendError(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setContentType("application/json");
        response.setStatus(status.value());
        response.getWriter().write(
            String.format("{\"error\": \"%s\", \"message\": \"%s\"}", status.getReasonPhrase(), message)
        );
    }
}
