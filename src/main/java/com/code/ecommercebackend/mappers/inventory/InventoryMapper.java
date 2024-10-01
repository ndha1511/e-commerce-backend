package com.code.ecommercebackend.mappers.inventory;

import com.code.ecommercebackend.dtos.request.inventory.InventoryDto;
import com.code.ecommercebackend.models.Inventory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    Inventory toInventory(InventoryDto inventoryDto);
}
