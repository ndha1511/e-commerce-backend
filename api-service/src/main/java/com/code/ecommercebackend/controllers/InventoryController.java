package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.request.inventory.CreateInventoryRequest;
import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.models.Inventory;
import com.code.ecommercebackend.services.inventory.InventoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("${api.prefix}/inventories")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public Response createInventory(@Valid @RequestBody CreateInventoryRequest createInventoryRequest,
                                    HttpServletRequest httpServletRequest)
    throws Exception {
        inventoryService.saveInventory(createInventoryRequest, httpServletRequest);
        return new ResponseSuccess<>(
                HttpStatus.NO_CONTENT.value(),
                "success"
        );
    }

    @GetMapping("/{variantId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public Response getPageInventoryByVariantId(@PathVariable String variantId,
                                                @RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "40") int size,
                                                @RequestParam(required = false) String[] search,
                                                @RequestParam(required = false) String[] sort) {
        List<String> searchList = new ArrayList<>();
        searchList.add("variantId=" + variantId);
        if(search != null) {
            searchList.addAll(Arrays.asList(search));
        }
        search = searchList.toArray(new String[0]);

        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                inventoryService.getPageData(page, size, search, sort, Inventory.class)
        );


    }

}
