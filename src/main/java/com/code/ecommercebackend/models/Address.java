package com.code.ecommercebackend.models;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
public class Address {
    @Field(name = "phone_number")
    private String phoneNumber;
    @Field(name = "house_number")
    private int houseNumber;
    private String ward;
    private String street;
    private String district;
    private String city;
    @Field(name = "address_detail")
    private String addressDetail;
    @Field(name = "default")
    private Boolean addressDefault;
}
