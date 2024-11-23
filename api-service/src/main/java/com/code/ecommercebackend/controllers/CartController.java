package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.request.cart.AddToCartRequest;
import com.code.ecommercebackend.dtos.request.cart.UpdateCartRequest;
import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.services.cart.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('USER')")
    public Response getCartByUserId(@PathVariable("userId") String userId)
     {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                cartService.getCartByUserId(userId)
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public Response addToCart(@Valid @RequestBody AddToCartRequest addToCartRequest) {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                cartService.addToCart(addToCartRequest)
        );
    }

    @PutMapping
    @PreAuthorize("hasRole('USER')")
    public Response updateCart(@Valid @RequestBody UpdateCartRequest updateCartRequest)
    throws Exception {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                cartService.updateCart(updateCartRequest)
        );
    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    public Response removeItemCart(@RequestParam String userId,
                                   @RequestParam String itemId)
    throws Exception {
        cartService.deleteCartItem(userId, itemId   );
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success"
        );
    }
}
