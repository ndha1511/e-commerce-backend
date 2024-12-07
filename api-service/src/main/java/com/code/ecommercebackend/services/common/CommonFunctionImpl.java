package com.code.ecommercebackend.services.common;

import com.code.ecommercebackend.models.User;
import com.code.ecommercebackend.models.ProductFeature;
import com.code.ecommercebackend.repositories.ProductFeatureRepository;
import com.code.ecommercebackend.repositories.UserRepository;
import com.code.ecommercebackend.services.auth.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CommonFunctionImpl implements CommonFunction {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ProductFeatureRepository productFeatureRepository;

    @Override
    public void saveUserBehavior(String token, long behavior, long productId, Float rating) {
        if (token != null && !token.isEmpty()) {
            String username = jwtService.extractUsername(token);
            User user = userRepository.findByUsername(username).orElse(null);
            ProductFeature productFeature = productFeatureRepository.findByProductIdAndUserIdIsNull(productId)
                    .orElse(null);
            if (productFeature != null) {
                if (user != null) {
                    ProductFeature productFeatureUser = productFeatureRepository.findByUserIdAndProductId(
                            user.getNumId(), productId
                    ).orElse(new ProductFeature());
                    productFeatureUser.setUserId(user.getNumId());
                    productFeatureUser.setProductId(productId);
                    productFeatureUser.setProductName(productFeature.getProductName());
                    productFeatureUser.setCategory(productFeature.getCategory());
                    productFeatureUser.setPrice(productFeature.getPrice());
                    productFeatureUser.setBrand(productFeature.getBrand());
                    if (behavior != 1) {
                        int countView = productFeatureUser.getCountView() != null ? productFeatureUser.getCountView() : 0;
                        productFeatureUser.setCountView(countView + 1);
                        productFeatureUser.setViewDate(LocalDateTime.now());
                    } else {
                        float oldRating = productFeatureUser.getRating() != null ? productFeatureUser.getRating() : 0f;
                        if (oldRating != 0) {
                            rating = (oldRating + rating) / 2;
                        }
                        productFeatureUser.setRating(rating);
                    }
                    productFeatureRepository.save(productFeatureUser);
                }
                if (behavior != 1) {
                    int countView = productFeature.getCountView() != null ? productFeature.getCountView() : 0;
                    productFeature.setCountView(countView + 1);
                    productFeatureRepository.save(productFeature);
                }
            }
        }
    }
}
