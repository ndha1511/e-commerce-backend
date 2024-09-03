package com.code.ecommercebackend.dtos.request.product;

import com.code.ecommercebackend.models.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
    @Min(value = 10000, message = "regular price must be greater than or equal to 10.000 vnd")
    @Max(value = 100000000, message = "regular price must be less than or equal to 100.000.000 vnd")
    private double regularPrice;
    @NotBlank(message = "store id must be not blank")
    private String shopId;
    private Set<String> categories;
    private String brandId;
    private String city;
    private int thumbnailIndex;
    private List<MultipartFile> images;
    private MultipartFile video;
    private List<ProductAttributeDto> attributesDto;
    @Valid
    @NotNull(message = "variant must be not null")
    private List<VariantDto> variantsDto;
    @NotBlank(message = "description must be not blank")
    private String description;
    private Set<Tag> tag;
}
