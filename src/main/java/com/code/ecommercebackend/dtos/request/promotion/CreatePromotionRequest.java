package com.code.ecommercebackend.dtos.request.promotion;

import com.code.ecommercebackend.models.Condition;
import com.code.ecommercebackend.models.enums.ApplyType;
import com.code.ecommercebackend.models.enums.DiscountType;
import com.code.ecommercebackend.models.enums.LoopState;
import com.code.ecommercebackend.models.enums.PromotionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class CreatePromotionRequest {
    @NotBlank(message = "promotion name must be not blank")
    private String promotionName;
    @NotNull(message = "promotion type must be not null")
    private PromotionType promotionType;
    @NotNull(message = "discount type must be not null")
    private DiscountType discountType;
    private Double discountValue;
    private Double maxPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LoopState loopState;
    private Condition condition;
    private ApplyType applyType;
    private Set<String> applyFor;
}
