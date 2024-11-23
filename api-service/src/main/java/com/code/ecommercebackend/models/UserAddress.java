package com.code.ecommercebackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user_addresses")
public class UserAddress extends Address {
    @Field(name = "user_id")
    private String userId;
    @Field(name = "phone_number")
    private String phoneNumber;
    @Field(name = "receiver_name")
    private String receiverName;
    @Field(name = "default")
    private Boolean addressDefault;
}
