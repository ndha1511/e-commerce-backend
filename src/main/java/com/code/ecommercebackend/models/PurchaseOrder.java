package com.code.ecommercebackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Set;

@Document(collection = "purchase_orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrder extends BaseModel {
    private Set<String> inventories;
    @Field(name = "total_quantity")
    private int totalQuantity;
    @Field(name = "total_price")
    private double totalPrice;
    @Field(name = "order_date")
    private LocalDateTime orderDate;
    private Address address;
}
