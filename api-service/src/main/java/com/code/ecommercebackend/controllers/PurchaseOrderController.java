package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.models.PurchaseOrder;
import com.code.ecommercebackend.services.purchaseOrder.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public Response getPagePurchaseOrders( @RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "40") int size,
                                           @RequestParam(required = false) String[] search,
                                           @RequestParam(required = false) String[] sort) {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                purchaseOrderService.getPageData(page,size, search, sort, PurchaseOrder.class)
        );

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public Response getPurchaseOrderById(@PathVariable String id) throws Exception {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                purchaseOrderService.findByPurchaseOrderId(id)
        );
    }
}
