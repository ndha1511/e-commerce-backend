package com.code.ecommercebackend.services.attribute;

import com.code.ecommercebackend.dtos.request.attribute.CreateAttributeRequest;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.exceptions.DataNotMatchedException;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.models.ProductAttribute;
import com.code.ecommercebackend.services.BaseService;

import java.io.IOException;

public interface AttributeService extends BaseService<ProductAttribute, String> {
    void save(CreateAttributeRequest createAttributeRequest) throws DataNotMatchedException, FileTooLargeException, FileNotSupportedException, IOException, DataNotFoundException;
}
