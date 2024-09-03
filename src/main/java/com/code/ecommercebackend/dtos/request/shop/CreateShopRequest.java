package com.code.ecommercebackend.dtos.request.shop;

import com.code.ecommercebackend.dtos.request.address.AddressDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateShopRequest {
    @NotBlank(message = "store name must be not blank")
    private String storeName;
    @NotBlank(message = "store name must be not blank")
    private String owner;
    @NotNull(message = "image must be not null")
    private MultipartFile image;
    @Valid
    @NotNull(message = "address must be not null")
    private AddressDto addressDto;
}
