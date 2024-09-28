package com.code.ecommercebackend.services.product;

import com.code.ecommercebackend.dtos.request.product.CreateProductRequest;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.models.Product;
import com.code.ecommercebackend.services.BaseService;

import java.io.IOException;

public interface ProductService extends BaseService<Product, String> {
    Product save(CreateProductRequest productDto) throws FileTooLargeException, FileNotSupportedException, IOException, DataNotFoundException;
}
