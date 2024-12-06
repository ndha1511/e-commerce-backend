package com.code.ecommercebackend.repositories.customizations.redis;

import com.code.ecommercebackend.dtos.response.PageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class RedisRepositoryImpl implements RedisRepository {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveDataInCache(String key, Object value) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(value);
        redisTemplate.opsForValue().set(key, json);
    }

    @Override
    public <T> T getDataFromCache(String key, Class<T> clazz) throws JsonProcessingException {
        String json = (String) redisTemplate.opsForValue().get(key);
        if (json != null) {
            JavaType type = objectMapper.getTypeFactory().constructType(clazz);
            return objectMapper.readValue(json, type);
        }
        return null;
    }

    @Override
    public <T> PageResponse<T> getPageDataInCache(String key, Class<T> clazz) throws JsonProcessingException {
        String json = (String) redisTemplate.opsForValue().get(key);
        if (json != null) {
            JavaType type = objectMapper.getTypeFactory().constructParametricType(PageResponse.class,
                    objectMapper.getTypeFactory().constructType(clazz));
            return objectMapper.readValue(json, type);
        }
        return null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void removeDataFromCache(String patternKey) {
        redisTemplate.execute((RedisCallback<Iterable<byte[]>>) connection -> {
            Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match(patternKey).build());
            while (cursor.hasNext()) {
                connection.del(cursor.next());
            }
            return null;
        });

    }
}
