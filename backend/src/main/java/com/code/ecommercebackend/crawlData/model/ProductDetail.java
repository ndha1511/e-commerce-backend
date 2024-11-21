package com.code.ecommercebackend.crawlData.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductDetail {
    private String description;
    private List<ProductImage> images;
    @JsonProperty("configurable_options")
    private List<Option> configurableOptions;
    @JsonProperty("video_url")
    private String videoUrl;
}
