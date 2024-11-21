package com.code.ecommercebackend.services.cart;

import com.code.ecommercebackend.dtos.request.cart.AddToCartRequest;
import com.code.ecommercebackend.dtos.request.cart.UpdateCartRequest;
import com.code.ecommercebackend.dtos.response.cart.ProductCartResponse;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.Cart;
import com.code.ecommercebackend.services.BaseService;

import java.util.List;

public interface CartService extends BaseService<Cart, String> {
    Cart addToCart(AddToCartRequest request);
    Cart updateCart(UpdateCartRequest request) throws DataNotFoundException;
    void deleteCartItem(String userId, String itemId) throws DataNotFoundException;
    List<ProductCartResponse> getCartByUserId(String userId);
}
