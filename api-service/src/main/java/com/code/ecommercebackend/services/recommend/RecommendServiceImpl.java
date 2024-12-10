package com.code.ecommercebackend.services.recommend;

import com.code.ecommercebackend.dtos.response.product.ProductResponse;
import com.code.ecommercebackend.models.ProductFeature;
import com.code.ecommercebackend.repositories.ProductFeatureRepository;
import com.code.ecommercebackend.repositories.customizations.redis.RedisRepository;
import com.code.ecommercebackend.services.product.ProductService;
import com.code.ecommercebackend.utils.RedisKeyHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    private final RedisRepository redisRepository;

    @Override
    public List<ProductResponse> getProductsRecommended(Long userId, Long productId, int nRecommend, String type) throws JsonProcessingException {
        String url;
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        List<Long> productIds;
        if(Objects.equals(type, "content-filtering")) {
            userId = 0L;
        }
        if(!productFeatureRepository.existsByUserId(userId)) {
            userId = 0L;
            if(productId == null || productId == 0L) {
                List<ProductFeature> productFeatures = productFeatureRepository.findTop5ByOrderByCountViewDesc();
                productIds = productFeatures.stream().map(ProductFeature::getProductId).toList();
            } else {
                productIds = List.of(productId);
            }
        } else {
            List<ProductFeature> productFeatures = productFeatureRepository.findTop5ByUserIdOrderByViewDateDesc(userId);
            productIds = productFeatures.stream().map(ProductFeature::getProductId).toList();
        }

        if(!productFeatureRepository.existsByUserIdAndRatingIsNotNull(userId)) {
            userId = 0L;
        }


        String productUrl = productIds.stream().map(id -> "&product_id=" + id)
                .reduce("", (accumulator, id) -> accumulator + id);

        String redisKey = RedisKeyHandler.createKeyRecommend(userId, productIds, nRecommend);

        List<ProductResponse> result = redisRepository.getListDataFromCache(redisKey, ProductResponse.class);

        if(result == null) {
            url =  recommendServiceUrl + "/recommend?user_id=" + userId + productUrl +"&top_n=" + nRecommend;

            ResponseEntity<List<Long>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {
                    }
            );
            List<Long> productIdsRs = Objects.requireNonNull(response.getBody());

            result = productService.getProductResponseByNumIds(productIdsRs);

            redisRepository.saveDataInCache(redisKey, result);

        }

        return result;
    }
}
