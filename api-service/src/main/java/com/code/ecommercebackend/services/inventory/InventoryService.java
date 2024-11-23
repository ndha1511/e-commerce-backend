package com.code.ecommercebackend.services.inventory;

import com.code.ecommercebackend.dtos.request.inventory.CreateInventoryRequest;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.InventoryDetail;
import com.code.ecommercebackend.services.BaseService;
import jakarta.servlet.http.HttpServletRequest;

public interface InventoryService extends BaseService<InventoryDetail, String> {
    void saveInventory(CreateInventoryRequest createInventoryRequest, HttpServletRequest request) throws DataNotFoundException;
}
