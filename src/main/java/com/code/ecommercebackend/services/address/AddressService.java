package com.code.ecommercebackend.services.address;

import com.code.ecommercebackend.models.Address;
import com.code.ecommercebackend.services.BaseService;

public interface AddressService extends BaseService<Address, String> {
    Address getAddress();
}
