package com.code.ecommercebackend.dtos.request.cart;

import com.code.ecommercebackend.models.ProductCart;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCartRequest {
    @NotBlank(message = "user id must be not blank")
    private String userId;
    private int index;
    private ProductCart productUpdate;
}
