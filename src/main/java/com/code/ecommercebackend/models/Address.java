package com.code.ecommercebackend.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "store_addresses")
public class Address {
    @Id
    private String id;
    private String ward;
    private String district;
    private String province;
    @Field(name = "address_detail")
    private String addressDetail;

}
