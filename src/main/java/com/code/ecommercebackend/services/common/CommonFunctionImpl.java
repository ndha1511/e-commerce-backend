package com.code.ecommercebackend.services.common;

import com.code.ecommercebackend.models.User;
import com.code.ecommercebackend.models.UserBehavior;
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
    public void saveUserBehavior(String token, long behavior, long productId, Integer quantity, Integer rating) {
        if(token != null && !token.isEmpty()) {
            String username = jwtService.extractUsername(token);
            User user = userRepository.findByUsername(username).orElse(null);
            if(user != null) {
                UserBehavior userBehavior = new UserBehavior();
                userBehavior.setBehavior(1);
                userBehavior.setUserId(user.getNumId());
                userBehavior.setProductId(productId);
                userBehavior.setRating(rating);
                userBehavior.setBuyQuantity(quantity);
                userBehaviorRepository.save(userBehavior);
            }
        }
    }
}
