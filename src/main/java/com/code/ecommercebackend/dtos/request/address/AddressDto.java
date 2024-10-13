package com.code.ecommercebackend.dtos.request.address;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto {
    @NotBlank(message = "district must be not blank")
    private String district;
    @NotBlank(message = "province must be not blank")
    private String province;
    @NotBlank(message = "ward must be not blank")
    private String ward;
    @NotBlank(message = "address detail must be not blank")
    private String addressDetail;
}
