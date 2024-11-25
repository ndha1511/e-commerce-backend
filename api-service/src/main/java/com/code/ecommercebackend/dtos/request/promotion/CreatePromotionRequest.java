package com.code.ecommercebackend.dtos.request.promotion;

import com.code.ecommercebackend.models.enums.DiscountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CreatePromotionRequest {
    @NotBlank(message = "promotion name must be not blank")
    private String promotionName;
    @NotNull(message = "discount type must be not null")
    private DiscountType discountType;
    private Double discountValue;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean view;
    private MultipartFile image;
    private List<String> applyFor;
}
