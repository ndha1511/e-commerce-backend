package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.request.inventory.CreateInventoryRequest;
import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.services.inventory.InventoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("${api.prefix}/inventories")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PostMapping
    public Response createInventory(@Valid @RequestBody CreateInventoryRequest createInventoryRequest,
                                    HttpServletRequest httpServletRequest)
    throws Exception {
        inventoryService.saveInventory(createInventoryRequest, httpServletRequest);
        return new ResponseSuccess<>(
                HttpStatus.NO_CONTENT.value(),
                "success"
        );
    }

}
