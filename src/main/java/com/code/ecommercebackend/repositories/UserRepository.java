package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
