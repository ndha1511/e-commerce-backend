package com.code.ecommercebackend.events;

import com.code.ecommercebackend.models.Category;
import com.code.ecommercebackend.repositories.customizations.redis.RedisRepository;
import com.code.ecommercebackend.utils.enums.RedisKeyEnum;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryListener extends AbstractMongoEventListener<Category> {
    private final RedisRepository redisRepository;

    @Override
    public void onAfterSave(@NonNull AfterSaveEvent<Category> event) {
        redisRepository.removeDataFromCache(RedisKeyEnum.CATEGORY.getValue() + "*");
    }
}
