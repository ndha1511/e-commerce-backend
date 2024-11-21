package com.code.ecommercebackend.dtos.request.attribute;

import jakarta.validation.constraints.Max;
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
    @Min(value = 10000, message = "price must be greater than or equal to 10.000 vnd")
    @Max(value = 100000000, message = "price must be less than or equal to 100.000.000 vnd")
    private double price;
    private String sku;
}
