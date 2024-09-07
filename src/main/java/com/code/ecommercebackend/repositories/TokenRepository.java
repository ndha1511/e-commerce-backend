package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepository extends MongoRepository<Token, String> {
    boolean existsByAccessToken(String accessToken);
    boolean existsByRefreshToken(String refreshToken);
}
