package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.services.recommend.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/recommend")
@RequiredArgsConstructor
public class RecommendController {
    private final RecommendService recommendService;


    @GetMapping("/{userId}")
    public Response getProductsRecommend(@PathVariable Long userId) {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                recommendService.getProductsRecommended(userId, 10)
        );
    }
}
