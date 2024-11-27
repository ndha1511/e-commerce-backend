package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.Order;
import com.code.ecommercebackend.services.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping
    public Response getOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "40") int size,
            @RequestParam(required = false) String[] search,
            @RequestParam(required = false) String[] sort
    ) {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                orderService.getPageData(page, size, search, sort, Order.class)
        );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{userId}")
    public Response getOrdersByUserId(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "40") int size,
            @RequestParam(required = false) String[] search,
            @RequestParam(required = false) String[] sort,
            @PathVariable String userId) {
        List<String> searchList = new ArrayList<>();
        searchList.add("userId=" + userId);
        if(search != null) {
            searchList.addAll(Arrays.asList(search));
        }
        search = searchList.toArray(new String[0]);
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                orderService.getPageData(page, size, search, sort, Order.class)
        );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/orderId/{orderId}")
    public Response getOrdersByOrderId(
            @PathVariable String orderId) throws DataNotFoundException {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                orderService.findById(orderId)
        );
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/confirm-received/{orderId}")
    public Response confirmReceived(@PathVariable String orderId)
            throws Exception {
        orderService.confirmReceived(orderId);
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success"
        );
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/confirm-cancel/{orderId}")
    public Response confirmCancel(@PathVariable String orderId)
            throws Exception {
        orderService.confirmCancel(orderId);
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success"
        );
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PutMapping("/confirm-shipping/{orderId}")
    public Response confirmShipping(@PathVariable String orderId)
            throws Exception {
        orderService.confirmShipping(orderId);
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success"
        );
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PutMapping("/confirm-shipped-confirmation/{orderId}")
    public Response confirm(@PathVariable String orderId)
            throws Exception {
        orderService.confirmShippedConfirmation(orderId);
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success"
        );
    }


}
