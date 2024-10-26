package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.request.brand.CreateBrandRequest;
import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.mappers.brand.BrandMapper;
import com.code.ecommercebackend.models.Brand;
import com.code.ecommercebackend.services.brand.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/brands")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;
    private final BrandMapper brandMapper;

    @PostMapping
    public Response createBrand(@Valid @ModelAttribute CreateBrandRequest createBrandRequest)
    throws Exception {
        Brand brand = brandMapper.toBrand(createBrandRequest);
        brand.createUrlPath();
        return new ResponseSuccess<>(
                HttpStatus.CREATED.value(),
                "success",
                brandService.save(brand)
        );
    }
}
