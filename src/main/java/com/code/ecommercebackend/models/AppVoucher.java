package com.code.ecommercebackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "app_vouchers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppVoucher extends Voucher {
    @Field(name = "max_discount_value")
    private double maxDiscountValue;
}
