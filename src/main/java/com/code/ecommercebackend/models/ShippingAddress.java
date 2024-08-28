package com.code.ecommercebackend.models;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Builder
public class ShippingAddress {
    @Field(name = "receiver_name")
    private String receiverName;
    private String address;
    @Field(name = "phone_number")
    private String phoneNumber;
}
