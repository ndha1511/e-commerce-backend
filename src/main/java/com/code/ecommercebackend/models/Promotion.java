package com.code.ecommercebackend.models;

import com.code.ecommercebackend.models.enums.ApplyType;
import com.code.ecommercebackend.models.enums.DiscountType;
import com.code.ecommercebackend.models.enums.LoopState;
import com.code.ecommercebackend.models.enums.PromotionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Set;

@Document(collection = "discounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Promotion extends BaseModel {
    @Field(name = "promotion_name")
    private String promotionName;
    @Field(name = "promotion_type")
    private PromotionType promotionType;
    @Field(name = "discount_type")
    private DiscountType discountType;
    @Field(name = "discount_value")
    private Double discountValue;
    @Field(name = "discount_price")
    private Double discountedPrice;
    @Field(name = "start_date")
    private LocalDateTime startDate;
    @Field(name = "end_date")
    private LocalDateTime endDate;
    @Field(name = "buy_x")
    private Integer buyX;
    @Field(name = "get_y")
    private Integer getY;
    @Field(name = "loop_state")
    private LoopState loopState;
    private Condition condition;
    @Field(name = "apply_type")
    private ApplyType applyType;
    @Field(name = "apply_for")
    private Set<String> applyFor;

}
