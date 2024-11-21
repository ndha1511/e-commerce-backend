package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    @Query("{$or: [{username: ?0}, {email:  ?0}, {phone_number:  ?0}]}")
    Optional<User> findByUsernameOrEmailOrPhoneNumber(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByPhoneNumber(String phoneNumber);
}
