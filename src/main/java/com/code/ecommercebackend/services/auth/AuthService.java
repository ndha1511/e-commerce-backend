package com.code.ecommercebackend.services.auth;

import com.code.ecommercebackend.dtos.request.auth.ChangePasswordRequest;
import com.code.ecommercebackend.dtos.request.auth.CreateNewPasswordRequest;
import com.code.ecommercebackend.dtos.request.auth.LoginRequest;
import com.code.ecommercebackend.dtos.request.auth.RegisterRequest;
import com.code.ecommercebackend.dtos.response.auth.TokenResponse;
import com.code.ecommercebackend.exceptions.*;
import com.code.ecommercebackend.models.User;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    void register(RegisterRequest registerRequest) throws DataNotMatchedException, MessagingException, DataExistsException;
    TokenResponse verifyEmail(String email, String otp) throws DataNotFoundException, DataNotMatchedException, DataExpiredException;
    TokenResponse login(LoginRequest loginRequest) throws DataNotFoundException, UserNotVerifyException, MessagingException;
    void sendOtp(String email) throws DataNotFoundException, MessagingException;
    void checkEmail(String email) throws DataExistsException;
    TokenResponse refreshToken(HttpServletRequest req, HttpServletResponse res) throws DataNotFoundException;
    User checkLogin(HttpServletRequest req, HttpServletResponse res);
    void logout(HttpServletRequest req, HttpServletResponse res);
    void resetPassword(String email) throws DataNotFoundException, MessagingException;
    void verifyEmailResetPassword(String email, String otp) throws DataNotFoundException, DataNotMatchedException, DataExpiredException;
    TokenResponse createNewPassword(CreateNewPasswordRequest createNewPasswordRequest) throws DataNotMatchedException, DataNotFoundException, UserNotVerifyException;
    TokenResponse changePassword(ChangePasswordRequest changePasswordRequest) throws DataNotFoundException, DataNotMatchedException;

}
