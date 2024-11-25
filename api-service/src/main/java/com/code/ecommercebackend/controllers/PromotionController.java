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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/promotions")
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;
    private final PromotionMapper promotionMapper;

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Response createPromotion(@Valid @ModelAttribute CreatePromotionRequest createPromotionRequest) {
        Promotion promotion = promotionMapper.toPromotion(createPromotionRequest);
        return new ResponseSuccess<>(
                HttpStatus.CREATED.value(),
                "success",
                promotionService.save(promotion)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Response getAllPromotions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "40") int size,
            @RequestParam(required = false) String[] search,
            @RequestParam(required = false) String[] sort
    ) {
        return new ResponseSuccess<>(
                HttpStatus.CREATED.value(),
                "success",
                promotionService.getPageData(page, size, search, sort, Promotion.class)
        );
    }

    @GetMapping("/carousel")
    public Response getPromotionCarousel() {
        return new ResponseSuccess<>(
                HttpStatus.CREATED.value(),
                "success",
                promotionService.getPromotionsCarousel()
        );
    }

    @GetMapping("/products/{promotionId}")
    public Response getProductsByPromotionId(@PathVariable String promotionId)
    throws Exception {
        return new ResponseSuccess<>(
                HttpStatus.CREATED.value(),
                "success",
                promotionService.getProductsByPromotionId(promotionId)
        );
    }
}
