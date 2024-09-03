package com.code.ecommercebackend.mappers.product.attribute;

import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.utils.S3Upload;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AttributeValueHelper {
    private final S3Upload s3Upload;

    @Named("attributeValueUpload")
    public String uploadImage(MultipartFile image) throws FileTooLargeException, FileNotSupportedException, IOException {
        return s3Upload.uploadImage(image);
    }
}
