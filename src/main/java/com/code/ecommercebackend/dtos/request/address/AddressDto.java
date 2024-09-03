package com.code.ecommercebackend.dtos.request.address;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto {
    @NotBlank(message = "phone number must be not blank")
    @Pattern(message = "phone number is invalid", regexp = "^0\\d{9}")
    private String phoneNumber;
    @NotBlank(message = "ward must be not blank")
    private String ward;
    @NotBlank(message = "street must be not blank")
    private String street;
    @NotBlank(message = "district must be not blank")
    private String district;
    @NotBlank(message = "city must be not blank")
    private String city;
    @NotBlank(message = "address detail must be not blank")
    private String addressDetail;
    private Boolean addressDefault;
}
