package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification,String> {
}
