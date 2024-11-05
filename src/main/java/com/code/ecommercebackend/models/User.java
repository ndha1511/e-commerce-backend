package com.code.ecommercebackend.models;

import com.code.ecommercebackend.models.enums.Gender;
import com.code.ecommercebackend.models.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseModel{
    private String username;
    @JsonIgnore
    private String password;
    @Indexed(unique = true)
    private String email;
    private String name;
    @Field(name = "phone_number")
    private String phoneNumber;
    private Gender gender;
    private String avatar;
    @JsonIgnore
    private Boolean verify;
    @Field(name = "verified_reset_password")
    @JsonIgnore
    private Boolean verifiedResetPassword;
    @Field(name = "google_account_id")
    @JsonIgnore
    private String googleAccountId;
    @Field(name = "facebook_account_id")
    @JsonIgnore
    private String facebookAccountId;
    @JsonIgnore
    private String otp;
    @JsonIgnore
    private LocalDateTime expiryDateOtp;
    private LocalDate dateOfBirth;
    private Set<Role> roles;
}
