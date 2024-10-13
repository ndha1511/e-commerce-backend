package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.UserAddress;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserAddressRepository extends MongoRepository<UserAddress, String> {
    List<UserAddress> findByUserIdOrderByAddressDefault(String userId);
    Optional<UserAddress> findByAddressDefault(boolean defaultAddress);
}
