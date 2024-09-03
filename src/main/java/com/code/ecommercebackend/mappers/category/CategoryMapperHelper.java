package com.code.ecommercebackend.mappers.category;

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
public class CategoryMapperHelper {
    private final S3Upload s3Upload;

    @Named("uploadCategoryImage")
    public String uploadCategoryImage(MultipartFile image) throws IOException,
            FileNotSupportedException, FileTooLargeException {
        return s3Upload.uploadImage(image);
    }
}
