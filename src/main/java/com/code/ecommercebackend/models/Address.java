package com.code.ecommercebackend.models;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
public class Address {
    @Field(name = "phone_number")
    private String phoneNumber;
    private String ward;
    private String street;
    private String district;
    private String city;
    @Field(name = "address_detail")
    private String addressDetail;
    @Field(name = "default")
    private Boolean addressDefault;
}
