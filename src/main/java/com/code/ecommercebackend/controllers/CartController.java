package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.request.cart.AddToCartRequest;
import com.code.ecommercebackend.dtos.request.cart.UpdateCartRequest;
import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.services.cart.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public Response getCartByUserId(@PathVariable("userId") String userId)
     {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                cartService.getCartByUserId(userId)
        );
    }

    @PostMapping
    public Response addToCart(@Valid @RequestBody AddToCartRequest addToCartRequest) {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                cartService.addToCart(addToCartRequest)
        );
    }

    @PutMapping
    public Response updateCart(@Valid @RequestBody UpdateCartRequest updateCartRequest)
    throws Exception {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                cartService.updateCart(updateCartRequest)
        );
    }
}
