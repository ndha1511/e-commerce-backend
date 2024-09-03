package com.code.ecommercebackend.dtos.request.product;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AttributeValueDto {
    @NotBlank(message = "attribute value not blank")
    private String value;
    private MultipartFile image;
}
