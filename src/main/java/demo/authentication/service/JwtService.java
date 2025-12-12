package demo.authentication.service;

import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

public interface JwtService {

    public String extractUsername(String token);

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver); 

    public String generateToken(String userDetails); 

    public String generateToken(
        Map<String, Object> extraClaims,
        String userDetails
    ); 

    public String generateRefreshToken(String userDetails);
    public boolean isTokenValid(String token, UserDetails userDetails); 
}
