package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.UserBehavior;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserBehaviorRepository extends MongoRepository<UserBehavior, String> {
    Optional<UserBehavior> findByUserIdAndProductId(long userId, long productId);
}
