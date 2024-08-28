package com.code.ecommercebackend.models;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
public class Tag {
    @Field(name = "tag_name")
    private String tagName;
    @Field(name = "tag_value")
    private String tagValue;
}
