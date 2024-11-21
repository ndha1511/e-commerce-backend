package com.code.ecommercebackend.services.address;

import com.code.ecommercebackend.dtos.request.address.UserAddressDto;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.mappers.address.UserAddressMapper;
import com.code.ecommercebackend.mappers.address.UserAddressMapperImpl;
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
    private final UserAddressMapper userAddressMapper;

    public UserAddressServiceImpl(MongoRepository<UserAddress, String> repository, UserAddressRepository userAddressRepository, UserAddressMapperImpl userAddressMapperImpl, UserAddressMapper userAddressMapper) {
        super(repository);
        this.userAddressRepository = userAddressRepository;
        this.userAddressMapper = userAddressMapper;
    }


    @Override
    public List<UserAddress> findByUserId(String userId) {
        return userAddressRepository.findByUserIdOrderByAddressDefault(userId);
    }

    @Override
    public UserAddress saveUserAddress(UserAddressDto userAddressDto) throws DataNotFoundException {
        UserAddress userAddress = userAddressMapper.toUserAddress(userAddressDto);
        validate(userAddress);
        return userAddress;
    }

    @Override
    public UserAddress updateAddress(UserAddressDto userAddressDto, String addressId) throws DataNotFoundException {
        UserAddress userAddress = userAddressRepository.findById(addressId)
                .orElseThrow(()-> new DataNotFoundException("address not found"));
        UserAddress userAddress1 = userAddressMapper.toUserAddress(userAddressDto);
        userAddress1.setId(userAddress.getId());
        validate(userAddress1);
        return userAddress1;
    }

    private void validate(UserAddress userAddress) throws DataNotFoundException {
        long count = userAddressRepository.countByUserId(userAddress.getUserId());
        boolean isUpdating = userAddress.getId() != null;
        if (!isUpdating && count >= 3) {
            throw new DataNotFoundException("Only 3 addresses can be created.");
        }
        if (count == 0) {
            userAddress.setAddressDefault(true);
            userAddressRepository.save(userAddress);
            return;
        }
        if (userAddress.getAddressDefault()) {
            Optional<UserAddress> optUserAddress = userAddressRepository.findByAddressDefaultAndUserId(true, userAddress.getUserId());
            if (optUserAddress.isPresent()) {
                UserAddress userAddress1 = optUserAddress.get();
                userAddress1.setAddressDefault(false);
                userAddressRepository.save(userAddress1);
            }

        }
        userAddressRepository.save(userAddress);
    }

}
