package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.request.address.AddressDto;
import com.code.ecommercebackend.dtos.request.address.UserAddressDto;
import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseGHN;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.dtos.response.address.DistrictResponse;
import com.code.ecommercebackend.dtos.response.address.ProvinceResponse;
import com.code.ecommercebackend.dtos.response.address.WardResponse;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.mappers.address.AddressMapper;
import com.code.ecommercebackend.mappers.address.UserAddressMapper;
import com.code.ecommercebackend.models.Address;
import com.code.ecommercebackend.models.UserAddress;
import com.code.ecommercebackend.services.address.AddressService;
import com.code.ecommercebackend.services.address.RestAddressService;
import com.code.ecommercebackend.services.address.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final RestAddressService restAddressService;
    private final UserAddressService userAddressService;
    private final UserAddressMapper userAddressMapper;
    private final AddressMapper addressMapper;
    private final AddressService addressService;

    @GetMapping("/provinces")
    public Response getProvinces() {
        ResponseGHN<List<ProvinceResponse>> response = restAddressService.getProvinces();
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                response.getData()
        );
    }
    @GetMapping("/districts")
    public Response getDistricts(@RequestParam(name = "province_id") int provinceId) {
        ResponseGHN<List<DistrictResponse>> response = restAddressService.getDistricts(provinceId);
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                response.getData()
        );
    }

    @GetMapping("/wards")
    public Response getWards(@RequestParam(name = "district_id") int districtId) {
        ResponseGHN<List<WardResponse>> response = restAddressService.getWards(districtId);
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                response.getData()
        );
    }


    @GetMapping("/{userId}")
    public Response findAddressesByUserId(@PathVariable String userId) {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                userAddressService.findByUserId(userId)
        );
    }

    @PostMapping("/add-user-address")
    public Response addUserAddress(@RequestBody UserAddressDto userAddressDto) throws DataNotFoundException {
        return new ResponseSuccess<>(
                HttpStatus.CREATED.value(),
                "success",
                userAddressService.saveUserAddress(userAddressDto)
        );
    }
    @PutMapping("/update-user-address/{id}")
    public Response upUserAddress(@RequestBody UserAddressDto userAddressDto, @PathVariable String id ) throws DataNotFoundException {
        return new ResponseSuccess<>(
                HttpStatus.CREATED.value(),
                "success",
                userAddressService.updateAddress(userAddressDto,id)
        );
    }
    @DeleteMapping("/delete-user-address/{id}")
    public Response removeUserAddress( @PathVariable String id ) throws DataNotFoundException {
        userAddressService.deleteById(id);
        return new ResponseSuccess<>(
                HttpStatus.CREATED.value(),
                "success"

        );
    }

    @PostMapping
    public Response addAddress(@RequestBody AddressDto addressDto) throws DataNotFoundException {
        Address address = addressMapper.toAddress(addressDto);
        return new ResponseSuccess<>(
                HttpStatus.CREATED.value(),
                "success",
                addressService.save(address)
        );
    }

    @GetMapping
    public Response getAddress() {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                addressService.getAddress()
        );
    }

}
