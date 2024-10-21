package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.models.Order;
import com.code.ecommercebackend.services.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    private final OrderService orderService;

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

    @PutMapping("/confirm-received/{orderId}")
    public Response confirmReceived(@PathVariable String orderId)
    throws Exception {
        orderService.confirmReceived(orderId);
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success"
        );
    }


}
