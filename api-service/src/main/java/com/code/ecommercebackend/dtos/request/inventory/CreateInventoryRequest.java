package com.code.ecommercebackend.dtos.request.inventory;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateInventoryRequest {
    List<InventoryDto> inventories;
}
