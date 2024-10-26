package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.request.voucher.VoucherRequest;
import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.mappers.voucher.VoucherMapper;
import com.code.ecommercebackend.services.voucher.VoucherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/vouchers")
@RequiredArgsConstructor
public class VoucherController {

    private final VoucherService voucherService;
    private final VoucherMapper voucherMapper;

    @PostMapping
    public Response createVoucher(@Valid @RequestBody VoucherRequest voucherRequest) {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                voucherService
                        .save(voucherMapper.toVoucher(voucherRequest))
        );
    }

    @GetMapping("/{userId}")
    public Response getVoucherById(@PathVariable String userId,
                                   @RequestParam(required = false, defaultValue = "1") int pageNo,
                                   @RequestParam(required = false, defaultValue = "20") int size) {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                voucherService.getVoucherByUserId(userId, pageNo, size)
        );

    }
}
