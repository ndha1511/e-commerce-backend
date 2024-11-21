package com.code.ecommercebackend.dtos.request.comment;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CommentReplyRequest {
    private String content;
    private MultipartFile files;
}
