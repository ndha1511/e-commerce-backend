package com.code.ecommercebackend.dtos.request.attribute;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AttributeValueDto {
    private String value;
    private MultipartFile image;
}
