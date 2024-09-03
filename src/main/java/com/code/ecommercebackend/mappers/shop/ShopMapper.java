package com.code.ecommercebackend.mappers.shop;

import com.code.ecommercebackend.dtos.request.shop.CreateShopRequest;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.models.Shop;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.io.IOException;

@Mapper(componentModel = "spring", uses = ShopMapperHelper.class)
public interface ShopMapper {
    @Mapping(source = "addressDto", target = "address", qualifiedByName = "mapAddress")
    @Mapping(source = "image", target = "logo", qualifiedByName = "uploadImage")
    Shop toShop(CreateShopRequest createShopRequest) throws FileNotSupportedException, FileTooLargeException, IOException;
}
