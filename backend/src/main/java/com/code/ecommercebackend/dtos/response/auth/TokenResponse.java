package com.code.ecommercebackend.dtos.response.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
}
