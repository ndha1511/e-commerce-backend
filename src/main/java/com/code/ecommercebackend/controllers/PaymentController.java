package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.request.payment.OrderRequest;
import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.services.payment.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/fee")
    public Response getFee(@RequestParam(name = "prick_province") String pickProvince,
                           @RequestParam(name = "prick_district") String pickDistrict,
                           @RequestParam String province,
                           @RequestParam String district,
                           @RequestParam int weight,
                           @RequestParam int value) {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                paymentService.calcFee(pickProvince, pickDistrict, province, district, weight, value)
        );

    }

    @PostMapping("/order")
    public Response order(@Valid @RequestBody OrderRequest orderRequest) throws Exception {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                paymentService.order(orderRequest)
        );
    }

}
