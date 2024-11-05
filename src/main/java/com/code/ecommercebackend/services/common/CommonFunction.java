package com.code.ecommercebackend.services.common;

public interface CommonFunction {
    void saveUserBehavior(String token,
                          long behavior,
                          long productId,
                          Integer quantity,
                          Integer rating);
}
