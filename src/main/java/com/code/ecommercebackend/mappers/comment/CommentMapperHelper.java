package com.code.ecommercebackend.mappers.comment;

import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.models.CommentMedia;
import com.code.ecommercebackend.models.User;
import com.code.ecommercebackend.models.enums.MediaType;
import com.code.ecommercebackend.repositories.UserRepository;
import com.code.ecommercebackend.utils.S3Upload;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CommentMapperHelper {
    private final S3Upload s3Upload;
    private final UserRepository userRepository;

    @Named("uploadCommentFile")
    public List<CommentMedia> uploadCommentFile(final List<MultipartFile> files)
            throws FileTooLargeException, FileNotSupportedException, IOException {
        List<CommentMedia> comments = new ArrayList<>();
        for (MultipartFile file : files) {
            CommentMedia commentMedia = new CommentMedia();
            MediaType mediaType = getMediaType(file);
            if (mediaType != null) {
                commentMedia.setMediaType(mediaType);
                String url = "";
                if (mediaType.equals(MediaType.IMAGE)) {
                    url = s3Upload.uploadImage(file);
                } else {
                    url = s3Upload.uploadVideo(file);
                }
                commentMedia.setPath(url);
                comments.add(commentMedia);
            }
        }
        return comments;
    }

    @Named("mapCommentUser")
    public User getUser(String userId) throws DataNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("user not found"));
    }

    private MediaType getMediaType(final MultipartFile file) {
        if(Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
            return MediaType.IMAGE;
        } else if(Objects.requireNonNull(file.getContentType()).startsWith("video/")) {
            return MediaType.VIDEO;
        }
        return null;
    }
}
