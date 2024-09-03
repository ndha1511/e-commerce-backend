package com.code.ecommercebackend.models;

import com.code.ecommercebackend.models.enums.MediaType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage extends BaseModel {
    @Field(name = "media_type")
    private MediaType mediaType;
    private String path;
}
