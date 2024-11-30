package com.code.ecommercebackend.dtos.request.brand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
@Getter
@Setter
public class UpdateBrandRequest {
    @NotBlank(message = "brand name must be not blank")
    private String brandName;
    private String description;
    private MultipartFile img;
    @NotEmpty(message = "categories must be not empty")
    private Set<String> categories;
}
