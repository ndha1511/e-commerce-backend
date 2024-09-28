package com.code.ecommercebackend.dtos.request.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariantDto {
    @NotBlank(message = "attribute value 1 must be not blank")
    private String attributeValue1;
    private String attributeValue2;
    private Double price;
    @Min(value = 1, message = "quantity must be greater than or equal to 1")
    private int quantity;
}
