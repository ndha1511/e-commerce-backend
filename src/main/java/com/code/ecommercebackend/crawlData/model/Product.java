package com.code.ecommercebackend.crawlData.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    private long id;
    private String name;
    @JsonProperty("url_path")
    private String urlPath;
    private long price;
    @JsonProperty("original_price")
    private long originalPrice;
    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;
}
