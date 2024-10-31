package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.UserBehavior;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserBehaviorRepository extends MongoRepository<UserBehavior, String> {
}
