package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {
}
