package com.code.ecommercebackend.models;

import com.code.ecommercebackend.models.enums.DiscountType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Voucher extends BaseModel {
    @Field(name = "voucher_name")
    private String voucherName;
    private String code;
    @Field(name = "discount_type")
    private DiscountType discountType;
    @Field(name = "discount_value")
    private double discountValue;
    @Field(name = "start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime startDate;
    @Field(name = "end_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime endDate;
    private int quantity;
    @Field(name = "min_order")
    private double minOrder;
    @Field(name = "max_price")
    private double maxPrice;
    @JsonIgnore
    @Field(name = "apply_all")
    private boolean applyAll;
    @JsonIgnore
    @Field(name = "apply_for")
    private Set<String> applyFor;
    private String image;
}
