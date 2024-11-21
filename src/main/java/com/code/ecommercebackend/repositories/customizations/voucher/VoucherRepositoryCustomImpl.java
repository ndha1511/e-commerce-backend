package com.code.ecommercebackend.repositories.customizations.voucher;

import com.code.ecommercebackend.dtos.response.PageResponse;
import com.code.ecommercebackend.models.Voucher;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VoucherRepositoryCustomImpl implements VoucherRepositoryCustom {

    private final MongoTemplate mongoTemplate;


    @Override
    public PageResponse<Voucher> getVoucherByUserId(String userId, int pageNo, int size) {
        Query query = new Query();
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("startDate").lt(java.time.LocalDateTime.now()),
                Criteria.where("endDate").gt(java.time.LocalDateTime.now()),
                new Criteria().orOperator(
                        Criteria.where("applyAll").is(true),
                        Criteria.where("applyFor").in(userId)
                )
        );
        Pageable pageable = PageRequest.of(pageNo - 1, size);
        query.addCriteria(criteria);
        query.with(pageable);
        long total = mongoTemplate.count(query, Voucher.class);
        List<Voucher> vouchers = mongoTemplate.find(query, Voucher.class);
        Page<Voucher> page = new PageImpl<>(vouchers, pageable, total);
        return new PageResponse<>(
                pageNo,
                vouchers.size(),
                page.getTotalPages(),
                page.getContent()
        );
    }
}
