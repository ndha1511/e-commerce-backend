package com.code.ecommercebackend.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
public class InventoryOrder {
    @Field(name = "inventory_id")
    private String inventoryId;
    private int quantity;
}
