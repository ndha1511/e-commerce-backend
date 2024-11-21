package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.VoucherUsage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VoucherUsageRepository extends MongoRepository<VoucherUsage, String> {
    boolean existsByVoucherIdAndUserId(String voucherId, String userId);
}
