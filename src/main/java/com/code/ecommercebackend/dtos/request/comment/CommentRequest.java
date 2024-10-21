package com.code.ecommercebackend.dtos.request.comment;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class CommentRequest {
    private String content;
    private List<MultipartFile> files;
    private int rating;
    private String productId;
    private List<String> attributes;
    private String userId;
}
