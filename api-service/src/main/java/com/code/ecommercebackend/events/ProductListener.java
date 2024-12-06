package com.code.ecommercebackend.events;

import com.code.ecommercebackend.models.Product;
import com.code.ecommercebackend.repositories.customizations.redis.RedisRepository;
import com.code.ecommercebackend.utils.enums.RedisKeyEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductListener extends AbstractMongoEventListener<Product> {
    private final RedisRepository redisRepository;

    @Override
    public void onAfterSave(AfterSaveEvent<Product> event) {
        Product product = event.getSource();
        redisRepository.removeDataFromCache(RedisKeyEnum.PRODUCT.getValue() + ":" + product.getUrlPath());
        redisRepository.removeDataFromCache(RedisKeyEnum.PRODUCTS.getValue() + "*");
    }
}
