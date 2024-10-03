package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.request.promotion.CreatePromotionRequest;
import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.mappers.promotion.PromotionMapper;
import com.code.ecommercebackend.models.Promotion;
import com.code.ecommercebackend.services.promotion.PromotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/promotions")
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;
    private final PromotionMapper promotionMapper;

    @PostMapping
    public Response createPromotion(@Valid @RequestBody CreatePromotionRequest createPromotionRequest) {
        Promotion promotion = promotionMapper.toPromotion(createPromotionRequest);
        return new ResponseSuccess<>(
                HttpStatus.CREATED.value(),
                "success",
                promotionService.save(promotion)
        );
    }
}
