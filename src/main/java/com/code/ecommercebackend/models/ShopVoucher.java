package com.code.ecommercebackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

@Document(collection = "shop_vouchers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShopVoucher extends Voucher {
    @Field(name = "store_id")
    private String storeId;
    @Field(name = "apply_for_products")
    private Set<String> applyForProducts;
}
