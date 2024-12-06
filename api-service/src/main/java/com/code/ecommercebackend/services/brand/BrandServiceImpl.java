package com.code.ecommercebackend.services.brand;


import com.code.ecommercebackend.dtos.request.brand.UpdateBrandRequest;
import com.code.ecommercebackend.dtos.response.PageResponse;
import com.code.ecommercebackend.exceptions.DataExistsException;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.mappers.brand.BrandMapper;
import com.code.ecommercebackend.models.Brand;
import com.code.ecommercebackend.repositories.BrandRepository;
import com.code.ecommercebackend.repositories.customizations.redis.RedisRepository;
import com.code.ecommercebackend.services.BaseServiceImpl;
import com.code.ecommercebackend.utils.RedisKeyHandler;
import com.code.ecommercebackend.utils.enums.RedisKeyEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class BrandServiceImpl extends BaseServiceImpl<Brand, String> implements BrandService {

    private final BrandMapper brandMapper;
    private final BrandRepository brandRepository;
    private final RedisRepository redisRepository;


    public BrandServiceImpl(MongoRepository<Brand, String> repository, BrandMapper brandMapper,
                            BrandRepository brandRepository, RedisRepository redisRepository) {
        super(repository);
        this.brandMapper = brandMapper;
        this.brandRepository = brandRepository;
        this.redisRepository = redisRepository;
    }

    @Override
    public Brand updateBrand(String idBrand, UpdateBrandRequest updateBrandRequest) throws DataNotFoundException, FileTooLargeException, FileNotSupportedException, IOException, DataExistsException {
        Brand brand = brandRepository.findById(idBrand).
                orElseThrow(()-> new DataNotFoundException("Brand not found"));
        Brand brandUpdate = brandMapper.toUpdateBrand(updateBrandRequest);
        if(brandUpdate.getImage() == null || brandUpdate.getImage().isEmpty()){
            brandUpdate.setImage(brand.getImage());
        }
        brandUpdate.setId(brand.getId());
        brandUpdate.createUrlPath();
        brandRepository.save(brandUpdate);
        return brandUpdate;
    }

    @Override
    public PageResponse<Brand> getPageData(int pageNo, int size, String[] search, String[] sort, Class<Brand> clazz) {
        String key = RedisKeyHandler.createKeyWithPageQuery(pageNo, size, search, sort, RedisKeyEnum.BRAND);
        try {
            PageResponse<Brand> result = redisRepository.getPageDataInCache(key, clazz);
            if(result == null) {
                result = super.getPageData(pageNo, size, search, sort, clazz);
                redisRepository.saveDataInCache(key, result);
            }
            return result;
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
