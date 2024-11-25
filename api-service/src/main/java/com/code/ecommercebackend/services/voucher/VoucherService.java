package com.code.ecommercebackend.services.voucher;

import com.code.ecommercebackend.dtos.response.PageResponse;
import com.code.ecommercebackend.models.Voucher;
import com.code.ecommercebackend.services.BaseService;


public interface VoucherService extends BaseService<Voucher, String> {
    PageResponse<Voucher> getVoucherByUserId(String userId, int page, int size);
}
