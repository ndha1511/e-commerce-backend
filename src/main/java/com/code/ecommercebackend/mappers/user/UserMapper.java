package com.code.ecommercebackend.mappers.user;

import com.code.ecommercebackend.dtos.request.category.CreateCategoryRequest;
import com.code.ecommercebackend.dtos.request.user.UserDto;
import com.code.ecommercebackend.exceptions.DataExistsException;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.mappers.UploadHelper;
import com.code.ecommercebackend.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.io.IOException;

@Mapper(componentModel = "spring", uses = {UploadHelper.class})
public interface UserMapper {
    @Mapping(source = "avatar", target = "avatar", qualifiedByName = "uploadImage")
    User toUser(UserDto userDto)
            throws FileTooLargeException, FileNotSupportedException, IOException, DataExistsException;
}
