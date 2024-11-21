package com.code.ecommercebackend.dtos.request.inventory;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryDto {
    @NotBlank(message = "product id must be not blank")
    private String productId;
    @NotBlank(message = "variant id must be not blank")
    private String variantId;
    @Min(value = 1, message = "import quantity must be greater than or equal 1")
    private int importQuantity;
    @Min(value = 10000, message = "price must be greater than or equal to 10.000 vnd")
    @Max(value = 100000000, message = "price must be less than or equal to 100.000.000 vnd")
    private double importPrice;


}
