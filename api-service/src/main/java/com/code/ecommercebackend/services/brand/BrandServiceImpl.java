package com.code.ecommercebackend.services.brand;


import com.code.ecommercebackend.dtos.request.brand.UpdateBrandRequest;
import com.code.ecommercebackend.exceptions.DataExistsException;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.mappers.brand.BrandMapper;
import com.code.ecommercebackend.models.Brand;
import com.code.ecommercebackend.repositories.BrandRepository;
import com.code.ecommercebackend.services.BaseServiceImpl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class BrandServiceImpl extends BaseServiceImpl<Brand, String> implements BrandService {

    private final BrandMapper brandMapper;
    private final BrandRepository brandRepository;


    public BrandServiceImpl(MongoRepository<Brand, String> repository, BrandMapper brandMapper,
                            BrandRepository brandRepository ) {
        super(repository);
        this.brandMapper = brandMapper;
        this.brandRepository = brandRepository;

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
}
