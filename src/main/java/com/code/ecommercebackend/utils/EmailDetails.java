package com.code.ecommercebackend.utils;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDetails {
    private String recipient;
    private String msgBody;
    private String subject;
    private MultipartFile attachment;
}
