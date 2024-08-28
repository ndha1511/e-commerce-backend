package com.code.ecommercebackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "voucher_usages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoucherUsage {
    @Id
    private String id;
    @Field(name = "voucher_id")
    private String voucherId;
    @Field(name = "user_id")
    private String userId;
}
