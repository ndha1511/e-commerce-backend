package com.code.ecommercebackend.events;

import com.code.ecommercebackend.models.Brand;
import com.code.ecommercebackend.repositories.customizations.redis.RedisRepository;
import com.code.ecommercebackend.utils.enums.RedisKeyEnum;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BrandListener extends AbstractMongoEventListener<Brand> {

    private final RedisRepository redisRepository;

    @Override
    public void onAfterSave(@NonNull AfterSaveEvent<Brand> event) {
        redisRepository.removeDataFromCache(RedisKeyEnum.BRAND.getValue() + "*");
    }
}
