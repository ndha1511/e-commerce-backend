package com.code.ecommercebackend.services.promotion;

import com.code.ecommercebackend.dtos.response.product.ProductResponse;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.Promotion;
import com.code.ecommercebackend.repositories.PromotionRepository;
import com.code.ecommercebackend.services.BaseServiceImpl;
import com.code.ecommercebackend.services.product.ProductService;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PromotionServiceImpl extends BaseServiceImpl<Promotion, String> implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final ProductService productService;

    @Override
    public Promotion save(Promotion promotion) {
        promotion.createUrlPath();
        return super.save(promotion);
    }

    public PromotionServiceImpl(MongoRepository<Promotion, String> repository, PromotionRepository promotionRepository, ProductService productService) {
        super(repository);
        this.promotionRepository = promotionRepository;
        this.productService = productService;
    }


    @Override
    public List<ProductResponse> getProductsByPromotionId(String promotionId) throws DataNotFoundException {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new DataNotFoundException("Promotion not found"));
        List<String> productIds = promotion.getApplyFor();
        return productService.getProductResponseById(productIds);
    }

    @Override
    public List<Promotion> getPromotionsCarousel() {
        return promotionRepository.findAllByCurrentDateAndView();
    }
}
