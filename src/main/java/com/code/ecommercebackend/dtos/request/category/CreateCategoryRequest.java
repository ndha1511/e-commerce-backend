package com.code.ecommercebackend.dtos.request.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateCategoryRequest {
    @NotBlank(message = "category name must be not blank")
    private String categoryName;
    private MultipartFile image;
    private String parentId;
}
