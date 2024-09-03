package com.code.ecommercebackend.services;

import com.code.ecommercebackend.dtos.response.PageResponse;
import com.code.ecommercebackend.exceptions.DataNotFoundException;

import java.util.function.Function;

public interface BaseService<T, ID> {
    T findById(ID id) throws DataNotFoundException;
    <DTO> DTO findDtoById(ID id, Function<T, DTO> mapper) throws DataNotFoundException;
    T save(T t);
    PageResponse<T> getPageData(int pageNo, int size, String[] search, String[] sort, Class<T> clazz);
}
