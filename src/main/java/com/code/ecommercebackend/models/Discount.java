package com.code.ecommercebackend.models;

import com.code.ecommercebackend.models.enums.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "discounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Discount extends BaseModel {
    @Field(name = "discount_type")
    private DiscountType discountType;
    @Field(name = "discount_value")
    private double discountValue;
    @Field(name = "discount_price")
    private double discountedPrice;
    @Field(name = "start_date")
    private LocalDateTime startDate;
    @Field(name = "end_date")
    private LocalDateTime endDate;
    @Field(name = "flash_sale")
    private boolean flashSale;
}
