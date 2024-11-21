package com.code.ecommercebackend.dtos.request.brand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@Setter
public class CreateBrandRequest {
    @NotBlank(message = "brand name must be not blank")
    private String brandName;
    private String description;
    private MultipartFile image;
    @NotEmpty(message = "categories must be not empty")
    private Set<String> categories;
}
