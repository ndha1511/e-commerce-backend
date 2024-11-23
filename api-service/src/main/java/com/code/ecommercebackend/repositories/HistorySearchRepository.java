package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.HistorySearch;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface HistorySearchRepository extends MongoRepository<HistorySearch, String> {
    Optional<HistorySearch> findByUserIdAndContent(String userId, String content);

}
