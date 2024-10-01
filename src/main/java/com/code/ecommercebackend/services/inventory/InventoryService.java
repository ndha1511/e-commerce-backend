package com.code.ecommercebackend.services.inventory;

import com.code.ecommercebackend.dtos.request.inventory.CreateInventoryRequest;
import com.code.ecommercebackend.models.Inventory;
import com.code.ecommercebackend.services.BaseService;

public interface InventoryService extends BaseService<Inventory, String> {
    void saveInventory(CreateInventoryRequest createInventoryRequest);
}
