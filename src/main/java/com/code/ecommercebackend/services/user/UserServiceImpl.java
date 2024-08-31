package com.code.ecommercebackend.services.user;

import com.code.ecommercebackend.models.User;
import com.code.ecommercebackend.services.BaseServiceImpl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, String> implements UserService {

    public UserServiceImpl(MongoRepository<User, String> repository) {
        super(repository);
    }
}
