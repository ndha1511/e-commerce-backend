package com.code.ecommercebackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "tokens")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Token extends BaseModel {
    @Field(name = "user_id")
    private String userId;
    @Field(name = "access_token")
    private String accessToken;
    @Field(name = "refresh_token")
    private String refreshToken;
    @Field(name = "expired_date")
    private LocalDateTime expiredDate;
}
