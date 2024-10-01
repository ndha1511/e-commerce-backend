package com.code.ecommercebackend.dtos.request.product;

import com.code.ecommercebackend.models.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class CreateProductRequest {
    @NotBlank(message = "product name must be not blank")
    private String productName;
    private Set<String> categories;
    private String brandId;
    @NotNull(message = "images must be not null")
    @NotEmpty(message = "images must be not empty")
    private List<MultipartFile> images;
    private MultipartFile video;
    @NotBlank(message = "description must be not blank")
    private String description;
    private Set<Tag> tags;
}
