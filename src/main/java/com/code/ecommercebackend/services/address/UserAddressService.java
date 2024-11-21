package com.code.ecommercebackend.services.address;

import com.code.ecommercebackend.dtos.request.address.UserAddressDto;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.UserAddress;
import com.code.ecommercebackend.services.BaseService;

import java.util.List;

public interface UserAddressService extends BaseService<UserAddress, String> {
    List<UserAddress> findByUserId(String userId);
    UserAddress saveUserAddress(UserAddressDto userAddressDto) throws DataNotFoundException;
    UserAddress updateAddress (UserAddressDto userAddressDto, String addressId) throws DataNotFoundException;
}
