package com.code.ecommercebackend.mappers.address;

import com.code.ecommercebackend.dtos.request.address.UserAddressDto;
import com.code.ecommercebackend.models.UserAddress;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAddressMapper {
    UserAddress toUserAddress(UserAddressDto userAddressDto);
}
