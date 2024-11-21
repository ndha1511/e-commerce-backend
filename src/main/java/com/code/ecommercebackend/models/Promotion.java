package com.code.ecommercebackend.models;

import com.code.ecommercebackend.models.enums.DiscountType;
import com.code.ecommercebackend.models.enums.LoopState;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
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
    @Field(name = "discount_type")
    private DiscountType discountType;
    @Field(name = "discount_value")
    private Double discountValue;
    @Field(name = "start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime startDate;
    @Field(name = "end_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime endDate;
    @Field(name = "loop_state")
    private LoopState loopState;
    @Field(name = "apply_all")
    private boolean applyAll;
    @Field(name = "apply_for")
    @Indexed
    private Set<String> applyFor;

}
