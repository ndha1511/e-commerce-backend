package com.code.ecommercebackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "notifications")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends BaseModel {
    @Field(name = "user_id")
    private String userId;
    private String image;
    private String content;
    private String title;
    private boolean seen;
    private LocalDateTime time;
    @Field(name = "redirect_to")
    private String redirectTo;
}
