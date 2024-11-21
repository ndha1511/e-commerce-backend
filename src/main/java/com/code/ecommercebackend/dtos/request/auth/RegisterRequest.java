package com.code.ecommercebackend.dtos.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @Pattern(message = "email is invalid", regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")
    @NotBlank(message = "email must be not blank")
    private String email;
    @Pattern(
            message = "Password must be at least 8 characters long, contain at least one uppercase letter, one digit, and one special character",
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.])[A-Za-z\\d@$!%*?&.]{8,}$"
    )
    @NotBlank(message = "Password must be not blank")
    private String password;
    private String confirmPassword;
}
