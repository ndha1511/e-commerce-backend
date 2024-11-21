package com.code.ecommercebackend.services.auth;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {
    String generateToken(UserDetails userDetails);
    String extractUsername(String token);
    boolean validateToken(String token, UserDetails userDetails);
    boolean validateRefreshToken(String refreshToken, UserDetails userDetails);
    String generateRefreshToken(Map<String, Object> claims, UserDetails userDetails);
}