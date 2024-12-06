package com.code.ecommercebackend.repositories.customizations.redis;

import com.code.ecommercebackend.dtos.response.PageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;


public interface RedisRepository {
    void saveDataInCache(String key, Object value) throws JsonProcessingException;
    <T> T getDataFromCache(String key, Class<T> clazz) throws JsonProcessingException;
    <T> PageResponse<T> getPageDataInCache(String key, Class<T> clazz) throws JsonProcessingException;
    void removeDataFromCache(String patternKey);

}
