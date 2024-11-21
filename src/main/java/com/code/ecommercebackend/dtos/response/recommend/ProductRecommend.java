package com.code.ecommercebackend.dtos.response.recommend;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRecommend {
    @JsonProperty("product_id")
    private long productId;
    @JsonProperty("predicted_rating")
    private String predictedRating;

}
