package com.code.ecommercebackend.models;

import com.code.ecommercebackend.models.enums.MediaType;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Field;


@Builder
public class CommentMedia {
    @Field(name = "media_type")
    private MediaType mediaType;
    private String path;

}
