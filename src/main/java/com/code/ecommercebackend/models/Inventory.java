package com.code.ecommercebackend.models;

import com.code.ecommercebackend.models.enums.InventoryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "inventories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inventory extends BaseModel {
    private String productId;
    private String variantId;
    @Field(name = "import_quantity")
    private int importQuantity;
    @Field(name = "sale_quantity")
    private int saleQuantity;
    @Field(name = "import_date")
    private LocalDateTime importDate;
    @Field(name = "import_price")
    private double importPrice;
    @Field(name = "inventory_status")
    private InventoryStatus inventoryStatus;

}
