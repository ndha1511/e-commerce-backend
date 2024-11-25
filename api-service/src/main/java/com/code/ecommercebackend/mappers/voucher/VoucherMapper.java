package com.code.ecommercebackend.mappers.voucher;

import com.code.ecommercebackend.dtos.request.voucher.VoucherRequest;
import com.code.ecommercebackend.mappers.UploadHelper;
import com.code.ecommercebackend.models.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UploadHelper.class})
public interface VoucherMapper {
    @Mapping(source = "image", target = "image", qualifiedByName = "uploadImage")
    Voucher toVoucher(VoucherRequest voucherRequest);
}
