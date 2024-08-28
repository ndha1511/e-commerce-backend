package com.code.ecommercebackend.models;

import com.code.ecommercebackend.models.enums.DiscountType;
import com.code.ecommercebackend.models.enums.VoucherType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class Voucher extends BaseModel {
    @Field(name = "voucher_name")
    private String voucherName;
    private String code;
    @Field(name = "discount_type")
    private DiscountType discountType;
    @Field(name = "discount_value")
    private double discountValue;
    @Field(name = "start_date")
    private LocalDateTime startDate;
    @Field(name = "end_date")
    private LocalDateTime endDate;
    @Field(name = "voucher_type")
    private VoucherType voucherType;
    private int quantity;
    @Field(name = "min_order")
    private double minOrder;
}
