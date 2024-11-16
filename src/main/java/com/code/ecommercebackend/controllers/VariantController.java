package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.services.variant.VariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/variants")
@RequiredArgsConstructor
public class VariantController {
    private final VariantService variantService;

    @GetMapping
    public Response getVariant(@RequestParam String productId,
                               @RequestParam String attr1,
                               @RequestParam(required = false) String attr2)
    throws Exception {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                variantService.findByProductIdAndAttribute(productId, attr1, attr2)
        );

    }

    @GetMapping("/{productId}")
    public Response getVariantByProductId(@PathVariable String productId) {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                variantService.findAllByProductId(productId)
        );
    }
    @GetMapping("/{productId}/byAttVal")
    public Response findAllByAttVal(
            @PathVariable String productId, // Nhận productId từ đường dẫn
            @RequestParam String attVl1 // Nhận attributeValue1 từ query parameter
    ) {
        return new ResponseSuccess<>(HttpStatus.OK.value(), "success", variantService.findAllByAttVal(productId, attVl1)); // Trả về ResponseSuccess
    }
}
