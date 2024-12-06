package com.code.ecommercebackend.utils.enums;

import lombok.Getter;

/**
 * PRODUCT = "product",
 * PRODUCTS = "products",
 * BRAND = "brand",
 * CATEGORY = "category"
 */
@Getter
public enum RedisKeyEnum {

    PRODUCT("product"),
    PRODUCTS("products"),
    BRAND("brand"),
    CATEGORY("category");

    private final String value;

    RedisKeyEnum(String value) {
        this.value = value;
    }
}
