package com.code.ecommercebackend.services.voucher;

import com.code.ecommercebackend.dtos.response.PageResponse;
import com.code.ecommercebackend.models.Voucher;

import com.code.ecommercebackend.repositories.customizations.voucher.VoucherRepositoryCustom;
import com.code.ecommercebackend.services.BaseServiceImpl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;



@Service
public class VoucherServiceImpl extends BaseServiceImpl<Voucher, String> implements VoucherService{
    private final VoucherRepositoryCustom voucherRepositoryCustom;

    public VoucherServiceImpl(MongoRepository<Voucher, String> repository,
                              VoucherRepositoryCustom voucherRepositoryCustom) {
        super(repository);
        this.voucherRepositoryCustom = voucherRepositoryCustom;
    }

    @Override
    public PageResponse<Voucher> getVoucherByUserId(String userId, int page, int size) {
        return voucherRepositoryCustom.getVoucherByUserId(userId, page, size);

    }


}
