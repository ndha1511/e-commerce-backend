package com.code.ecommercebackend.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class BaseModel {
    @Id
    private String id;
    @Field(name = "created_at")
    private LocalDateTime createdAt;
    @Field(name = "updated_at")
    private LocalDateTime updatedAt;

}
