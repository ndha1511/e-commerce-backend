package com.code.ecommercebackend.services.address;

import com.code.ecommercebackend.dtos.response.ResponseGHN;
import com.code.ecommercebackend.dtos.response.address.DistrictResponse;
import com.code.ecommercebackend.dtos.response.address.ProvinceResponse;
import com.code.ecommercebackend.dtos.response.address.WardResponse;

import java.util.List;

public interface RestAddressService {
    ResponseGHN<List<ProvinceResponse>> getProvinces();
    ResponseGHN<List<DistrictResponse>> getDistricts(int provinceId);
    ResponseGHN<List<WardResponse>> getWards(int districtId);
}
