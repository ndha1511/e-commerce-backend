package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.Address;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AddressRepository extends MongoRepository<Address, String> {
}
