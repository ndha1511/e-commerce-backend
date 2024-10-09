package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.request.inventory.CreateInventoryRequest;
import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.services.inventory.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/inventories")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping
    public Response createInventory(@Valid @RequestBody CreateInventoryRequest request)
    throws Exception {
        inventoryService.saveInventory(request);
        return new ResponseSuccess<>(
                HttpStatus.NO_CONTENT.value(),
                "success"
        );
    }
}
