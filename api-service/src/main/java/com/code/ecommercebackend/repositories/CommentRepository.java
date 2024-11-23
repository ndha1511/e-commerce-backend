package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String> {

}
