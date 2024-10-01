package com.code.ecommercebackend.services.product;

import com.code.ecommercebackend.models.*;
import com.code.ecommercebackend.services.BaseServiceImpl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;


@Service
public class ProductServiceImpl extends BaseServiceImpl<Product, String> implements ProductService {

    public ProductServiceImpl(
            MongoRepository<Product, String> repository) {
        super(repository);
    }

}
