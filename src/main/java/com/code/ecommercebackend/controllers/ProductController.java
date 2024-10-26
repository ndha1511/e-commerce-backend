package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.request.attribute.CreateAttributeRequest;
import com.code.ecommercebackend.dtos.request.product.CreateProductRequest;
import com.code.ecommercebackend.dtos.request.product.ImportExcelRequest;
import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.mappers.product.ProductMapper;
import com.code.ecommercebackend.models.Product;
import com.code.ecommercebackend.services.attribute.AttributeService;
import com.code.ecommercebackend.services.excel.ExcelService;
import com.code.ecommercebackend.services.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final AttributeService attributeService;
    private final ExcelService excelService;

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
                productService.getPageProduct(page, size, search, sort)
        );
    }

    @PostMapping
    public Response createProduct(@Valid @ModelAttribute CreateProductRequest createProductRequest)
    throws Exception {
        Product product = productMapper.toProduct(createProductRequest);
        product.createUrlPath();
        product.setThumbnail(product.getImages().get(0));
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

    @GetMapping("/attributes/{productId}")
    public Response getProductAttributes(@PathVariable String productId) {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                attributeService.findByProductId(productId)
        );
    }

    @GetMapping("/{urlPath}")
    public Response findByUrlPath(@PathVariable String urlPath)
    throws Exception {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                productService.findByUrl(urlPath)
        );
    }

    @GetMapping("/download-sample-excel")
    public ResponseEntity<InputStreamResource> downloadSampleExcel(
            @RequestParam String categoryId
    ) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=example.xlsx");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(excelService.generateExcelImportProduct(categoryId)));
    }

    @PostMapping("/import-excel")
    public Response importExcel(@ModelAttribute ImportExcelRequest importExcelRequest) throws Exception {
        excelService.importProductExcel(importExcelRequest.getFile());
        return new ResponseSuccess<>(
                HttpStatus.NO_CONTENT.value(),
                "success"
        );
    }
}
