package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.models.User;
import com.code.ecommercebackend.services.auth.JwtService;
import com.code.ecommercebackend.services.recommend.RecommendService;
import com.code.ecommercebackend.services.user.UserService;
import com.code.ecommercebackend.utils.CookieHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/recommend")
@RequiredArgsConstructor
public class RecommendController {
    private final RecommendService recommendService;
    private final UserService userService;
    private final JwtService jwtService;
    private final CookieHandler cookieHandler;


    @GetMapping()
    public Response getProductsRecommend(HttpServletRequest request,
                                         @RequestParam(required = false) Long productId,
                                         @RequestParam(required = false, defaultValue = "10") int nRecommend,
                                         @RequestParam(required = false, defaultValue = "content-filtering") String type) {
        String token = cookieHandler.getCookie(request, "refresh_token");
        String username = null;
        if(token != null && !token.isEmpty()) {
            username = jwtService.extractUsername(cookieHandler.getCookie(request, "refresh_token"));
        }
        long userId = 0;
        if (username != null && !username.isEmpty()) {
            User user = userService.findByUsername(username);
            if (user != null) {
                userId = user.getNumId();
            }

        }
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                recommendService.getProductsRecommended(userId, productId, nRecommend, type)
        );
    }
}
