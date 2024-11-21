package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.Voucher;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.Optional;

public interface VoucherRepository extends MongoRepository<Voucher, String> {
    Optional<Voucher> findByCode(String code);
}
