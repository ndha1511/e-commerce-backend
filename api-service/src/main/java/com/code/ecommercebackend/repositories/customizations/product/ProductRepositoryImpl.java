package com.code.ecommercebackend.repositories.customizations.product;

import com.code.ecommercebackend.dtos.response.PageResponse;
import com.code.ecommercebackend.models.Product;
import com.code.ecommercebackend.repositories.customizations.PageQueryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryImpl extends PageQueryRepository<Product> implements ProductRepositoryCustom {
    @Override
    public PageResponse<Product> getPageData(int pageNo, int size, String[] search, String[] sort,
                                             String rangeRegularPrice,
                                             String rangeRating) {
        Query query = new Query();
        if (search != null) {
            for (String s : search) {
                addConditionQuery(query, s);
            }
        }
        if(rangeRegularPrice != null) {
            String[] rangeRegularPriceSplit = rangeRegularPrice.split("-");
            if(rangeRegularPriceSplit.length == 2) {
                query.addCriteria(Criteria.where("regularPrice").gte(Double.parseDouble(rangeRegularPriceSplit[0]))
                        .lte(Double.parseDouble(rangeRegularPriceSplit[1])));
            }
        }
        if(rangeRating != null) {
            String[] rangeRatingSplit = rangeRating.split("-");
            if(rangeRatingSplit.length == 2) {
                query.addCriteria(Criteria.where("rating").gte(Integer.parseInt(rangeRatingSplit[0]))
                        .lte(Integer.parseInt(rangeRatingSplit[1])));
            }
        }
        if (sort != null) {
            for (String s : sort) {
                addSort(query, s);
            }
        }

        long total = mongoTemplate.count(query, Product.class);
        // define $skip: (pageNo - 1) * size $limit: size
        Pageable pageable = PageRequest.of(pageNo - 1, size);
        // apply $skip $limit
        query.with(pageable);
        List<Product> result = mongoTemplate.find(query, Product.class);

        Page<Product> page = new PageImpl<>(result, pageable, total);

        return new PageResponse<>(
                pageNo,
                result.size(),
                page.getTotalPages(),
                page.getContent()
        );
    }
}
