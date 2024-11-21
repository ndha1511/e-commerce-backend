package com.code.ecommercebackend.dtos.request.product;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ImportExcelRequest {
    private MultipartFile file;
}
