package com.code.ecommercebackend.mappers.address;

import com.code.ecommercebackend.dtos.request.address.AddressDto;
import com.code.ecommercebackend.models.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toAddress(AddressDto addressDto);
}
