package com.code.ecommercebackend.dtos.request.user;

import com.code.ecommercebackend.models.enums.Gender;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserDto {
    private String username;
    private String email;
    private String name;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private MultipartFile avatar;
    private Gender gender;
}
