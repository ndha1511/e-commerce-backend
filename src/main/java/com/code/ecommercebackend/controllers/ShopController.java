package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.request.shop.CreateShopRequest;
import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.mappers.shop.ShopMapper;
import com.code.ecommercebackend.models.Shop;
import com.code.ecommercebackend.services.shop.ShopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/shops")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService shopService;
    private final ShopMapper shopMapper;

    @PostMapping
    public Response createShop(@Valid @ModelAttribute CreateShopRequest shopDto)
    throws Exception {
        Shop shop = shopMapper.toShop(shopDto);
        shop.createUrlPath();
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                shopService.save(shop)
        );
    }
}
