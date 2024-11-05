package com.code.ecommercebackend.dtos.request.employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeAccount {
    @NotBlank(message = "email must be not blank")
    @Pattern(message = "email is invalid", regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")
    private String email;
    @NotBlank(message = "name must be not blank")
    private String name;
    @NotBlank(message = "password must be not blank")
    private String password;
}
