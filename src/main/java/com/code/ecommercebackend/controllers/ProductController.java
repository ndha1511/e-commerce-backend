package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.request.attribute.CreateAttributeRequest;
import com.code.ecommercebackend.dtos.request.product.CreateProductRequest;
import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.mappers.product.ProductMapper;
import com.code.ecommercebackend.models.Product;
import com.code.ecommercebackend.services.attribute.AttributeService;
import com.code.ecommercebackend.services.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final AttributeService attributeService;

    @GetMapping
    public Response getProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "40") int size,
            @RequestParam(required = false) String[] search,
            @RequestParam(required = false) String[] sort
    ) {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                productService.getPageData(page, size, search, sort, Product.class)
        );
    }

    @PostMapping
    public Response createProduct(@Valid @ModelAttribute CreateProductRequest createProductRequest)
    throws Exception {
        Product product = productMapper.toProduct(createProductRequest);
        product.createUrlPath();
        return new ResponseSuccess<>(
                HttpStatus.CREATED.value(),
                "success",
                productService.save(product)
        );
    }

    @PostMapping("/attributes")
    public Response createProductAttributes(@Valid @ModelAttribute CreateAttributeRequest createAttributeRequest)
        throws Exception {
        attributeService.save(createAttributeRequest);
        return new ResponseSuccess<>(
                HttpStatus.NO_CONTENT.value(),
                "success"
        );
    }
}
