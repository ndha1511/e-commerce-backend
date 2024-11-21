package com.code.ecommercebackend.utils;

import com.code.ecommercebackend.dtos.response.auth.TokenResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieHandler {
    @Value("${jwt.expiry-date}")
    private int expiryDate;
    @Value("${jwt.expiry-date-refresh-token}")
    private int expiryDateRefreshToken;

    public void addCookie(HttpServletResponse response, String name, String value, int age) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        if(age >= 0) cookie.setMaxAge(age);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public String getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public void setCookieAuth(HttpServletResponse res, TokenResponse tokenResponse) {
        addCookie(res,
                "access_token",
                tokenResponse.getAccessToken(),
                expiryDate);
        addCookie(res,
                "refresh_token",
                tokenResponse.getRefreshToken(),
                expiryDateRefreshToken);
    }
}
