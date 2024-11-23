package com.code.ecommercebackend.dtos.request.message;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class MessageDto {
    private String message;
    private MultipartFile file;
    @NotBlank(message = "sender is not blank")
    private String sender;
    @NotBlank(message = "receiver is not blank")
    private String receiver;
}
