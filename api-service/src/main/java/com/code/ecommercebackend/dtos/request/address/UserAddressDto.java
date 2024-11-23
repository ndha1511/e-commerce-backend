package com.code.ecommercebackend.dtos.request.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAddressDto extends AddressDto {
    @NotBlank(message = "user id must be not blank")
    private String userId;
    @NotBlank(message = "receiver name must be not blank")
    private String receiverName;
    @Pattern(message = "phone number is invalid", regexp = "^0\\d{9}")
    private String phoneNumber;
    private Boolean addressDefault;
}
