package com.code.ecommercebackend.services;

import com.code.ecommercebackend.dtos.response.PageResponse;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.repositories.customizations.PageQueryRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.Serializable;
import java.util.function.Function;

public class BaseServiceImpl<T, ID extends Serializable> extends PageQueryRepository<T> implements BaseService<T, ID> {

    private final MongoRepository<T, ID> repository;


    public BaseServiceImpl(MongoRepository<T, ID> repository) {
        this.repository = repository;

    }

    @Override
    public T findById(ID id) throws DataNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("user not found"));
    }

    @Override
    public <DTO> DTO findDtoById(ID id, Function<T, DTO> mapper) throws DataNotFoundException {
        T t = findById(id);
        return mapper.apply(t);
    }

    @Override
    public T save(T t)  {
        return repository.save(t);
    }

    @Override
    public PageResponse<T> getPageData(int pageNo, int size, String[] search, String[] sort, Class<T> clazz) {
        return super.getPageData(pageNo, size, search, sort, clazz);
    }
    @Override
    public void deleteById(ID id) {
        repository.deleteById(id);
    }

}
