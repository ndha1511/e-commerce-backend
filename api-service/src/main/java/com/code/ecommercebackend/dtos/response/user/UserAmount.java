package com.code.ecommercebackend.dtos.response.user;

import com.code.ecommercebackend.models.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAmount {
    private User user;
    private double amount;
}
