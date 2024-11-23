package com.code.ecommercebackend.mappers.inventory;

import com.code.ecommercebackend.dtos.request.inventory.InventoryDto;
import com.code.ecommercebackend.models.InventoryDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    InventoryDetail toInventory(InventoryDto inventoryDto);
}
