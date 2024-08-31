package com.code.ecommercebackend.mappers;

import com.code.ecommercebackend.dtos.request.UserRequest;
import com.code.ecommercebackend.models.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest userRequest);
}
