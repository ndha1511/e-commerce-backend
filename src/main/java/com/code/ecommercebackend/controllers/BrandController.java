package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.request.brand.CreateBrandRequest;
import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.mappers.brand.BrandMapper;
import com.code.ecommercebackend.models.Brand;
import com.code.ecommercebackend.models.Category;
import com.code.ecommercebackend.services.brand.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/brands")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;
    private final BrandMapper brandMapper;

    @PreAuthorize("hasRole('ADMIN')")
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

    @GetMapping
    public Response getBrands(@RequestParam(defaultValue = "1") int pageNo,
                                  @RequestParam(defaultValue = "40") int size,
                                  @RequestParam(required = false) String[] search,
                                  @RequestParam(required = false) String[] sort) {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                brandService.getPageData(pageNo, size, search, sort, Brand.class)
        );
    }
}
