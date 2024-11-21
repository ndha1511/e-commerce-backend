package com.code.ecommercebackend.services.user;

import com.code.ecommercebackend.dtos.request.user.UserDto;
import com.code.ecommercebackend.exceptions.DataExistsException;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.models.User;
import com.code.ecommercebackend.services.BaseService;

import java.io.IOException;

public interface UserService extends BaseService<User, String> {
    User update(String email, UserDto userDto) throws DataNotFoundException, DataExistsException, FileTooLargeException, FileNotSupportedException, IOException;
    User findByUsername(String username);
}
