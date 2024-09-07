package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.request.auth.LoginRequest;
import com.code.ecommercebackend.dtos.request.auth.RegisterRequest;
import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.services.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

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
            @RequestParam String otp
    ) throws Exception {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                authService.verifyEmail(email, otp)
        );
    }

    @PostMapping("/login")
    public Response login(@RequestBody @Valid final LoginRequest loginRequest)
    throws Exception {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                authService.login(loginRequest)
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
}
