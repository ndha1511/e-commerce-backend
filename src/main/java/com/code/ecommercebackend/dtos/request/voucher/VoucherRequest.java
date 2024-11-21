package com.code.ecommercebackend.dtos.request.voucher;

import com.code.ecommercebackend.models.enums.DiscountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class VoucherRequest {
    @NotBlank(message = "voucher name must be not blank")
    private String voucherName;
    @NotBlank(message = "code must be not blank")
    private String code;
    @NotNull(message = "discount type must be not null")
    private DiscountType discountType;
    private double discountValue;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int quantity;
    private double minOrder;
    private double maxPrice;
    private boolean applyAll;
    private Set<String> applyFor;
}
