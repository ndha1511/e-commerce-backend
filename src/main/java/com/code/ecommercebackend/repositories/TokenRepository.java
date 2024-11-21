package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends MongoRepository<Token, String> {
    boolean existsByAccessToken(String accessToken);
    boolean existsByRefreshToken(String refreshToken);
    Optional<Token> findByRefreshToken(String refreshToken);
    List<Token> findAllByUserId(String userId);
}
