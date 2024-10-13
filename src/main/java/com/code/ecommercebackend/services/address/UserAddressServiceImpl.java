package com.code.ecommercebackend.services.address;

import com.code.ecommercebackend.models.UserAddress;
import com.code.ecommercebackend.repositories.UserAddressRepository;
import com.code.ecommercebackend.services.BaseServiceImpl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserAddressServiceImpl extends BaseServiceImpl<UserAddress, String> implements UserAddressService {

    private final UserAddressRepository userAddressRepository;

    public UserAddressServiceImpl(MongoRepository<UserAddress, String> repository, UserAddressRepository userAddressRepository) {
        super(repository);
        this.userAddressRepository = userAddressRepository;
    }

    @Override
    public UserAddress save(UserAddress userAddress) {
        Optional<UserAddress> optUserAddress = userAddressRepository.findByAddressDefault(true);
        if(optUserAddress.isPresent()) {
            UserAddress userAddress1 = optUserAddress.get();
            userAddress1.setAddressDefault(false);
            userAddressRepository.save(userAddress1);
        } else {
            userAddress.setAddressDefault(true);
        }
        return super.save(userAddress);
    }

    @Override
    public List<UserAddress> findByUserId(String userId) {
        return userAddressRepository.findByUserIdOrderByAddressDefault(userId);
    }
}
