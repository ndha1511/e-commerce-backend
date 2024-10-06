package com.code.ecommercebackend.services.product;

import com.code.ecommercebackend.dtos.response.PageResponse;
import com.code.ecommercebackend.dtos.response.product.ProductResponse;
import com.code.ecommercebackend.mappers.product.ProductMapper;
import com.code.ecommercebackend.models.*;
import com.code.ecommercebackend.repositories.PromotionRepository;
import com.code.ecommercebackend.services.BaseServiceImpl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


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
        List<ProductResponse> productResponses = mapToProductResponses(products);
        PageResponse<ProductResponse> pageResponse = new PageResponse<>();
        pageResponse.setItems(productResponses);
        pageResponse.setPageSize(pageProduct.getPageSize());
        pageResponse.setPageNumber(pageProduct.getPageNumber());
        pageResponse.setTotalPage(pageProduct.getTotalPage());
        return pageResponse;
    }


    public List<ProductResponse> mapToProductResponses(List<Product> products) {
        List<ProductResponse> productResponses = new java.util.ArrayList<>(products
                .stream().map(productMapper::toProductResponse).toList());
        int index = 0;
        for (Product product : products) {
            Optional<Promotion> opPromotion = promotionRepository.findFirstByCurrentDateAndProductId(product.getId());
            if(opPromotion.isPresent()) {
                Promotion promotion = opPromotion.get();
                ProductResponse newProductResponse = productResponses.get(index);
                newProductResponse.setPromotion(promotion);
                productResponses.set(index, newProductResponse);
            }
            index++;
        }
        return productResponses;
    }

}
