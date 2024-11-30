package com.code.ecommercebackend.services.brand;

import com.code.ecommercebackend.dtos.request.brand.UpdateBrandRequest;
import com.code.ecommercebackend.exceptions.DataExistsException;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.models.Brand;
import com.code.ecommercebackend.services.BaseService;

import java.io.IOException;

public interface BrandService extends BaseService<Brand, String> {
    Brand updateBrand(String idBrand, UpdateBrandRequest updateBrandRequest) throws DataNotFoundException, FileTooLargeException, FileNotSupportedException, IOException, DataExistsException;
}
