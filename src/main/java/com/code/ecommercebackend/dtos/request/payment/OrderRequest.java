package com.code.ecommercebackend.dtos.request.payment;

import com.code.ecommercebackend.models.UserAddress;
import com.code.ecommercebackend.models.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {
    @NotEmpty(message = "order items must be not empty")
    private List<OrderItem> orderItems;
    @NotBlank(message = "user id must be not blank")
    private String userId;
    private String voucherCode;
    @NotNull(message = "payment method must be not null")
    private PaymentMethod paymentMethod;
    @NotNull(message = "user address must be not null")
    private UserAddress userAddress;
    @NotBlank(message = "order form must be not blank")
    private String orderFrom;
    private String note;
    private double deliveryFee;
}
