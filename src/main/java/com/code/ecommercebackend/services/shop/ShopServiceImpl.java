package com.code.ecommercebackend.services.shop;

import com.code.ecommercebackend.models.Shop;
import com.code.ecommercebackend.services.BaseServiceImpl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public class ShopServiceImpl extends BaseServiceImpl<Shop, String> implements ShopService {
    public ShopServiceImpl(MongoRepository<Shop, String> repository) {
        super(repository);
    }
}
