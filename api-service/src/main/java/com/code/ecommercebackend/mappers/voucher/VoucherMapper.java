package com.code.ecommercebackend.mappers.voucher;

import com.code.ecommercebackend.dtos.request.voucher.VoucherRequest;
import com.code.ecommercebackend.models.Voucher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VoucherMapper {
    Voucher toVoucher(VoucherRequest voucherRequest);
}
