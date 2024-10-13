package com.code.ecommercebackend.services.address;

import com.code.ecommercebackend.models.UserAddress;
import com.code.ecommercebackend.services.BaseService;

import java.util.List;

public interface UserAddressService extends BaseService<UserAddress, String> {
    List<UserAddress> findByUserId(String userId);
}
