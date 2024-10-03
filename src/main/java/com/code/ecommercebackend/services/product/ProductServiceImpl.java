package com.code.ecommercebackend.services.product;

import com.code.ecommercebackend.dtos.response.PageResponse;
import com.code.ecommercebackend.dtos.response.product.ProductResponse;
import com.code.ecommercebackend.mappers.product.ProductMapper;
import com.code.ecommercebackend.models.*;
import com.code.ecommercebackend.models.enums.DiscountType;
import com.code.ecommercebackend.models.enums.PromotionType;
import com.code.ecommercebackend.repositories.PromotionRepository;
import com.code.ecommercebackend.services.BaseServiceImpl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class ProductServiceImpl extends BaseServiceImpl<Product, String> implements ProductService {

    private final ProductMapper productMapper;
    private final PromotionRepository promotionRepository;

    public ProductServiceImpl(
            MongoRepository<Product, String> repository,
            ProductMapper productMapper, PromotionRepository promotionRepository) {
        super(repository);
        this.productMapper = productMapper;
        this.promotionRepository = promotionRepository;
    }

    @Override
    public PageResponse<ProductResponse> getPageProduct(int pageNo, int size, String[] search, String[] sort) {
        PageResponse<Product> pageProduct = super.getPageData(pageNo, size, search, sort, Product.class);
        List<Product> products = pageProduct.getItems();
        List<ProductResponse> productResponses = new java.util.ArrayList<>(products
                .stream().map(productMapper::toProductResponse).toList());
        List<Promotion> promotions = promotionRepository.findAllByPromotionType(PromotionType.DIRECT_DISCOUNT);
        int index = 0;
        for (Product product : products) {
            Set<String> categories = product.getCategories();
            Optional<Promotion> opPromotion = promotions.stream()
                    .filter(promotion -> !Collections.disjoint(promotion.getApplyFor(), categories) ||
                            !Collections.disjoint(promotion.getApplyFor(), Set.of(product.getId())) ||
                            !Collections.disjoint(promotion.getApplyFor(), Set.of(product.getBrandId())))
                    .findFirst();
            if(opPromotion.isPresent()) {
                Promotion promotion = opPromotion.get();
                ProductResponse newProductResponse = productResponses.get(index);
                if(promotion.getDiscountType().equals(DiscountType.PERCENT)) {
                    newProductResponse.setDiscountPercent(promotion.getDiscountValue().floatValue());
                    newProductResponse.setDiscountedPrice(newProductResponse.getRegularPrice() -
                            (newProductResponse.getRegularPrice() * promotion.getDiscountValue()));
                } else {
                    newProductResponse.setDiscountedPrice(promotion.getDiscountValue());
                    BigDecimal roundedDiscountPercent = BigDecimal.valueOf(promotion.getDiscountValue()/product.getRegularPrice())
                            .setScale(5, RoundingMode.HALF_UP);
                    newProductResponse.setDiscountPercent(roundedDiscountPercent.floatValue());
                }
                productResponses.set(index, newProductResponse);
            }
            index++;
        }
        PageResponse<ProductResponse> pageResponse = new PageResponse<>();
        pageResponse.setItems(productResponses);
        pageResponse.setPageSize(pageProduct.getPageSize());
        pageResponse.setPageNumber(pageProduct.getPageNumber());
        pageResponse.setTotalPage(pageProduct.getTotalPage());
        return pageResponse;
    }
}
