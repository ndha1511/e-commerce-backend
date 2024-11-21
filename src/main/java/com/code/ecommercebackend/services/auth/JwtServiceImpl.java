package com.code.ecommercebackend.services.auth;

import com.code.ecommercebackend.repositories.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.expiry-date}")
    private long expiryDate;
    @Value("${jwt.expiry-date-refresh-token}")
    private long expiryDateRefreshToken;
    @Value("${jwt.secret}")
    private String secret;
    private final TokenRepository tokenRepository;


    @Override
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * expiryDate)))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        boolean existByAccessToken = tokenRepository.existsByAccessToken(token);
        final String username = extractClaim(token, Claims::getSubject);
        return username.equals(userDetails.getUsername()) && expiredDateToken(token) && existByAccessToken;
    }

    @Override
    public boolean validateRefreshToken(String refreshToken, UserDetails userDetails) {
        boolean existByRefreshToken = tokenRepository.existsByRefreshToken(refreshToken);
        final String username = extractClaim(refreshToken, Claims::getSubject);
        return username.equals(userDetails.getUsername()) && expiredDateToken(refreshToken) && existByRefreshToken;
    }

    private boolean expiredDateToken(String token) {
        return !extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private Key getSignKey() {
        byte[] keys = Decoders.BASE64URL.decode(secret);
        return Keys.hmacShaKeyFor(keys);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey())
                .build().parseClaimsJws(token).getBody();
    }

    @Override
    public String generateRefreshToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * expiryDateRefreshToken)))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
