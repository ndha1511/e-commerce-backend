package com.code.ecommercebackend.services.brand;

import com.code.ecommercebackend.models.Brand;
import com.code.ecommercebackend.services.BaseServiceImpl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public class BrandServiceImpl extends BaseServiceImpl<Brand, String> implements BrandService {

    public BrandServiceImpl(MongoRepository<Brand, String> repository) {
        super(repository);
    }
}
