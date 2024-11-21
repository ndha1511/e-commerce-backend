package com.code.ecommercebackend.services.recommend;

import com.code.ecommercebackend.dtos.response.product.ProductResponse;
import com.code.ecommercebackend.dtos.response.recommend.ProductRecommend;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {
    @Value("${recommend-system-url}")
    private String recommendServiceUrl;
    private final ProductService productService;
    private final RestTemplate restTemplate;

    @Override
    public List<ProductRecommend> getProductsRecommended(long userId, int nRecommend) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = recommendServiceUrl + "/recommend?user_id=" + userId + "&n_recommend=" + nRecommend;
        ResponseEntity<List<ProductRecommend>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {
                }
        );

        return response.getBody();
    }
}
