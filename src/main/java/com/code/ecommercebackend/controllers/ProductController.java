package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.request.product.CreateProductRequest;
import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.services.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public Response createProduct(@Valid @ModelAttribute CreateProductRequest createProductRequest)
    throws Exception {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                productService.save(createProductRequest)
        );
    }
}
