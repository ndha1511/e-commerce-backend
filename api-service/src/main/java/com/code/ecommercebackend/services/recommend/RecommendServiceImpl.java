package com.code.ecommercebackend.services.recommend;

import com.code.ecommercebackend.dtos.response.product.ProductResponse;
import com.code.ecommercebackend.models.ProductFeature;
import com.code.ecommercebackend.repositories.ProductFeatureRepository;
import com.code.ecommercebackend.repositories.customizations.productFeature.CategoryCount;
import com.code.ecommercebackend.repositories.customizations.productFeature.ProductFeatureRepositoryCustom;
import com.code.ecommercebackend.services.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {
    @Value("${recommend-system-url}")
    private String recommendServiceUrl;
    private final ProductService productService;
    private final RestTemplate restTemplate;
    private final ProductFeatureRepository productFeatureRepository;
    private final ProductFeatureRepositoryCustom productFeatureRepositoryCustom;

    @Override
    public List<ProductResponse> getProductsRecommended(Long userId, Long productId, int nRecommend, String type) {
        String url;
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        if(Objects.equals(type, "content-filtering")) {
            userId = 0L;
        }
        if(!productFeatureRepository.existsByUserId(userId)) {
            userId = 0L;
            if(productId == null || productId == 0L) {
                ProductFeature productFeature = productFeatureRepository.findTopByOrderByCountViewDesc();
                productId = productFeature.getProductId();
            }
        } else {
            ProductFeature productFeature;
            List<CategoryCount> categoryCounts = productFeatureRepositoryCustom.findLargestCategoryGroups(userId);
            List<CategoryCount> categoryCountSort =
                    categoryCounts.stream().sorted(Comparator.comparingInt(CategoryCount::getCount).reversed())
                    .toList();
            String category = categoryCountSort.get(0).getId();
            List<ProductFeature> productFeatures = productFeatureRepository.findAllByCategoryAndUserId(category, userId);
            List<ProductFeature> sortedProductFeatures = productFeatures.stream()
                    .sorted(Comparator.comparingInt(ProductFeature::getCountView).reversed())
                    .toList();
            List<ProductFeature> productFeatureRating = sortedProductFeatures.stream().filter(p -> p.getRating() != null)
                    .toList();
            if(productFeatureRating.isEmpty()) {
                productFeature = sortedProductFeatures.get(0);
            } else {
                productFeature = productFeatureRating.get(0);
            }
            productId = productFeature.getProductId();
            if(productFeature.getRating() == null) {
                userId = 0L;
            }
        }

        url =  recommendServiceUrl + "/recommend?user_id=" + userId + "&product_id=" + productId +"&top_n=" + nRecommend;

        ResponseEntity<List<Long>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {
                }
        );
        List<Long> productIds = Objects.requireNonNull(response.getBody());

        return productService.getProductResponseByNumIds(productIds);
    }
}
