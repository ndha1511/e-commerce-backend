package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.Voucher;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface VoucherRepository extends MongoRepository<Voucher, String> {
}
