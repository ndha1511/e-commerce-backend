package com.code.ecommercebackend.services.common;

import com.code.ecommercebackend.models.User;
import com.code.ecommercebackend.models.ProductFeature;
import com.code.ecommercebackend.repositories.UserBehaviorRepository;
import com.code.ecommercebackend.repositories.UserRepository;
import com.code.ecommercebackend.services.auth.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommonFunctionImpl implements CommonFunction {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserBehaviorRepository userBehaviorRepository;

    @Override
    public void saveUserBehavior(String token, long behavior, long productId, Integer rating) {
        if(token != null && !token.isEmpty()) {
            String username = jwtService.extractUsername(token);
            User user = userRepository.findByUsername(username).orElse(null);
            if(user != null) {
                ProductFeature userBehavior = userBehaviorRepository.findByUserIdAndProductId(
                        user.getNumId(), productId
                ).orElse(new ProductFeature());
                userBehavior.setUserId(user.getNumId());
                userBehavior.setProductId(productId);
                if(behavior != 1) {
                    userBehavior.setCountView(userBehavior.getCountView() + 1);
                } else {
                    userBehavior.setRating(rating);
                }
                userBehaviorRepository.save(userBehavior);
            }
        }
    }
}
