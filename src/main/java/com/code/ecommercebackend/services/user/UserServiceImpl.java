package com.code.ecommercebackend.services.user;

import com.code.ecommercebackend.dtos.request.user.UserDto;
import com.code.ecommercebackend.exceptions.DataExistsException;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.mappers.user.UserMapper;
import com.code.ecommercebackend.models.User;
import com.code.ecommercebackend.repositories.UserRepository;
import com.code.ecommercebackend.services.BaseServiceImpl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, String> implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(MongoRepository<User, String> repository, UserRepository userRepository, UserMapper userMapper) {
        super(repository);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }



        @Override
        public User update(String email, UserDto userDto) throws DataNotFoundException, DataExistsException, FileTooLargeException, FileNotSupportedException, IOException {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User not found"));
            User userUpdate = userMapper.toUser(userDto);
            user.setGender(userUpdate.getGender());
            user.setAvatar(userUpdate.getAvatar());
            user.setName(userUpdate.getName());
            user.setUsername(userUpdate.getUsername());
            user.setPhoneNumber(userUpdate.getPhoneNumber());
            user.setDateOfBirth(userUpdate.getDateOfBirth());
            return userRepository.save(user);
        }

    @Override
    public User findByUsername(String username)  {
        if(username == null) return null;
        return userRepository.findByUsername(username).orElseThrow(null);
    }
}
