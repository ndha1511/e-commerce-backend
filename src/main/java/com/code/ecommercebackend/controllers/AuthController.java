package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.request.auth.ChangePasswordRequest;
import com.code.ecommercebackend.dtos.request.auth.CreateNewPasswordRequest;
import com.code.ecommercebackend.dtos.request.auth.LoginRequest;
import com.code.ecommercebackend.dtos.request.auth.RegisterRequest;
import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.dtos.response.auth.TokenResponse;
import com.code.ecommercebackend.models.User;
import com.code.ecommercebackend.services.auth.AuthService;
import com.code.ecommercebackend.utils.CookieHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;
    private final CookieHandler cookieHandler;


    @PostMapping("/register")
    public Response register(@RequestBody @Valid final RegisterRequest registerRequest)
    throws Exception {
        authService.register(registerRequest);
        return new ResponseSuccess<>(
                HttpStatus.CREATED.value(),
                "success"
        );
    }

    @GetMapping("/verify-email")
    public Response verifyEmail(
            @RequestParam String email,
            @RequestParam String otp,
            HttpServletResponse res
    ) throws Exception {
        TokenResponse tokenResponse = authService.verifyEmail(email, otp);
        cookieHandler.setCookieAuth(res, tokenResponse);
        return new ResponseSuccess<>(
                HttpStatus.NO_CONTENT.value(),
                "success"
        );
    }

    @PostMapping("/login")
    public Response login(@RequestBody @Valid final LoginRequest loginRequest, HttpServletResponse res)
    throws Exception {
        TokenResponse tokenResponse = authService.login(loginRequest);
        cookieHandler.setCookieAuth(res, tokenResponse);
        return new ResponseSuccess<>(
                HttpStatus.NO_CONTENT.value(),
                "success"
        );
    }

    @GetMapping("/check-email")
    public Response checkEmail(@RequestParam String email)
    throws Exception {
        authService.checkEmail(email);
        return new ResponseSuccess<>(
                HttpStatus.NO_CONTENT.value(),
                "success"
        );
    }

    @GetMapping("/send-otp")
    public Response sendOtp(@RequestParam String email) throws Exception {
        authService.sendOtp(email);
        return new ResponseSuccess<>(
                HttpStatus.NO_CONTENT.value(),
                "success"
        );
    }

    @GetMapping("/refresh-token")
    public Response refreshToken(HttpServletRequest req, HttpServletResponse res)
    throws Exception {
        TokenResponse tokenResponse = authService.refreshToken(req, res);
        cookieHandler.setCookieAuth(res, tokenResponse);
        return new ResponseSuccess<>(
                HttpStatus.NO_CONTENT.value(),
                "success"
        );
    }

    @GetMapping("/check-login")
    public Response checkLogin(HttpServletRequest req, HttpServletResponse res) {
        User user = authService.checkLogin(req, res);
        if(user == null) {
            cookieHandler.addCookie(res, "access_token", "", 0);
            cookieHandler.addCookie(res, "refresh_token", "", 0);
        }
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                user
        );

    }

    @GetMapping("/reset-password")
    public Response resetPassword(@RequestParam String email)
    throws Exception {
        authService.resetPassword(email);
        return new ResponseSuccess<>(
                HttpStatus.NO_CONTENT.value(),
                "success"
        );
    }

    @GetMapping("/verify-email-reset-password")
    public Response verifyEmailResetPassword(
            @RequestParam String email,
            @RequestParam String otp
    ) throws Exception {
        authService.verifyEmailResetPassword(email, otp);
        return new ResponseSuccess<>(
                HttpStatus.NO_CONTENT.value(),
                "success"
        );
    }

    @PostMapping("/create-new-password")
    public Response createNewPassword(@RequestBody @Valid CreateNewPasswordRequest request
    , HttpServletResponse res)
    throws Exception {
        TokenResponse tokenResponse = authService.createNewPassword(request);
        cookieHandler.setCookieAuth(res, tokenResponse);
        return new ResponseSuccess<>(
                HttpStatus.NO_CONTENT.value(),
                "success"
        );
    }



    @PostMapping("/change-password")
    public Response changePassword(@RequestBody @Valid ChangePasswordRequest request, HttpServletResponse res)
    throws Exception {
        TokenResponse tokenResponse = authService.changePassword(request);
        cookieHandler.setCookieAuth(res, tokenResponse);
        return new ResponseSuccess<>(
                HttpStatus.NO_CONTENT.value(),
                "success"
        );
    }

    @GetMapping("/logout")
    public Response logout(HttpServletRequest req, HttpServletResponse res) {
        authService.logout(req, res);
        return new ResponseSuccess<>(
                HttpStatus.NO_CONTENT.value(),
                "success"
        );
    }



}
