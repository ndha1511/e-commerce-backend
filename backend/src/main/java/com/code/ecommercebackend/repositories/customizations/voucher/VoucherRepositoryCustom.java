package com.code.ecommercebackend.repositories.customizations.voucher;

import com.code.ecommercebackend.dtos.response.PageResponse;
import com.code.ecommercebackend.models.Voucher;


public interface VoucherRepositoryCustom {
    PageResponse<Voucher> getVoucherByUserId(String userId, int page, int size);
}
