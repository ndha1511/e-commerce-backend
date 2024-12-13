package com.code.ecommercebackend.utils.enums;

import lombok.Getter;

/**
 * PRODUCT = "product",
 * PRODUCTS = "products",
 * BRAND = "brand",
 * CATEGORY = "category",
 * RECOMMEND = "recommend",
 * HISTORY_IMPORT = "history_import",
 * HISTORIES_IMPORT = "histories_import"
 */
@Getter
public enum RedisKeyEnum {

    PRODUCT("product"),
    PRODUCTS("products"),
    BRAND("brand"),
    CATEGORY("category"),
    RECOMMEND("recommend"),
    HISTORY_IMPORT("history_import"),
    HISTORIES_IMPORT("histories_import"),;

    private final String value;

    RedisKeyEnum(String value) {
        this.value = value;
    }
}
