package com.code.ecommercebackend.oauth2;

import com.code.ecommercebackend.components.LocalDateTimeVN;
import com.code.ecommercebackend.dtos.response.auth.TokenResponse;
import com.code.ecommercebackend.models.Token;
import com.code.ecommercebackend.models.User;
import com.code.ecommercebackend.models.UserDetail;
import com.code.ecommercebackend.models.enums.Role;
import com.code.ecommercebackend.repositories.TokenRepository;
import com.code.ecommercebackend.repositories.UserRepository;
import com.code.ecommercebackend.services.auth.JwtService;
import com.code.ecommercebackend.utils.CookieHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class Oauth2SuccessLogin implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final CookieHandler cookieHandler;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    @Value("${front-end-url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        OAuth2User principal = (OAuth2User) authentication.getPrincipal();

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        String registrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
        User user = new User();
        user.setPhoneNumber("");
        user.setVerify(true);
        user.setPassword("");
        user.setNumId(userRepository.count() + 1);
        user.generateUsername();
        user.setRoles(Set.of(Role.ROLE_USER));
        if (registrationId.equals("google")) {
            GoogleAccount googleAccount = new GoogleAccount(
                    principal.getName(),
                    Objects.requireNonNull(principal.getAttribute("name")).toString(),
                    Objects.requireNonNull(principal.getAttribute("email")).toString(),
                    Objects.requireNonNull(principal.getAttribute("picture")).toString()
            );
            user.setEmail(googleAccount.getEmail());
            user.setName(googleAccount.getName());
            user.setGoogleAccountId(googleAccount.getAccountId());
            user.setAvatar(googleAccount.getPictureUrl());
        }

        User userDb = userRepository.findByEmail(user.getEmail()).orElse(user);
        if(userDb.getFacebookAccountId() == null) userDb.setFacebookAccountId(user.getFacebookAccountId());
        if(userDb.getFacebookAccountId() == null) userDb.setGoogleAccountId(user.getGoogleAccountId());
        userRepository.save(userDb);
        TokenResponse tokenResponse = saveToken(userDb);
        cookieHandler.setCookieAuth(response, tokenResponse);
        response.sendRedirect(frontendUrl);
    }

    private TokenResponse saveToken(User user) {
        List<Token> tokens = tokenRepository.findAllByUserId(user.getId());
        if(!tokens.isEmpty() && tokens.size() >= 2) {
            Token tokenDelete = tokens.get(tokens.size() - 1);
            tokenRepository.delete(tokenDelete);
        }
        UserDetail userDetail = new UserDetail(user);
        Token token = Token.builder()
                .accessToken(jwtService.generateToken(userDetail))
                .refreshToken(jwtService.generateRefreshToken(new HashMap<>(), userDetail))
                .userId(user.getId())
                .expiredDate(LocalDateTimeVN.now().plusDays(30))
                .build();
        tokenRepository.save(token);
        return TokenResponse.builder()
                .accessToken(jwtService.generateToken(userDetail))
                .refreshToken(jwtService.generateRefreshToken(new HashMap<>(), userDetail))
                .build();
    }




}

