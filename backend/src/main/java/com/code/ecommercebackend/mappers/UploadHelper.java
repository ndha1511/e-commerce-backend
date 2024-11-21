package com.code.ecommercebackend.mappers;

import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.utils.S3Upload;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UploadHelper {
    private final S3Upload s3Upload;

    @Named("uploadImage")
    public String uploadImage(MultipartFile image) throws IOException,
            FileNotSupportedException, FileTooLargeException {
        return s3Upload.uploadImage(image);
    }

    @Named("uploadImages")
    public List<String> uploadImages(List<MultipartFile> images)
            throws IOException, FileNotSupportedException, FileTooLargeException {
        List<String> imagePaths = new ArrayList<>();
        for (MultipartFile image : images) {
            imagePaths.add(s3Upload.uploadImage(image));
        }
        return imagePaths;
    }

    @Named("uploadVideo")
    public String uploadVideo(MultipartFile video)
            throws FileTooLargeException, FileNotSupportedException, IOException {
        if(video != null) {
            return s3Upload.uploadVideo(video);
        }
        return null;
    }
}
