package com.code.ecommercebackend.services.auth;

import com.code.ecommercebackend.dtos.request.auth.LoginRequest;
import com.code.ecommercebackend.dtos.request.auth.RegisterRequest;
import com.code.ecommercebackend.dtos.response.auth.TokenResponse;
import com.code.ecommercebackend.exceptions.DataExistsException;
import com.code.ecommercebackend.exceptions.DataExpiredException;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.exceptions.DataNotMatchedException;
import jakarta.mail.MessagingException;

public interface AuthService {
    void register(RegisterRequest registerRequest) throws DataNotMatchedException, MessagingException, DataExistsException;
    TokenResponse verifyEmail(String email, String otp) throws DataNotFoundException, DataNotMatchedException, DataExpiredException;
    TokenResponse login(LoginRequest loginRequest) throws DataNotFoundException;
    void sendOtp(String email) throws DataNotFoundException, MessagingException;
    void checkEmail(String email) throws DataExistsException;

}
