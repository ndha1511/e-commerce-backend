package com.code.ecommercebackend.services.promotion;

import com.code.ecommercebackend.dtos.request.promotion.CreatePromotionRequest;
import com.code.ecommercebackend.mappers.promotion.PromotionMapper;
import com.code.ecommercebackend.models.Promotion;
import com.code.ecommercebackend.services.BaseServiceImpl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public class PromotionServiceImpl extends BaseServiceImpl<Promotion, String> implements PromotionService {

    public PromotionServiceImpl(MongoRepository<Promotion, String> repository) {
        super(repository);
    }



}
