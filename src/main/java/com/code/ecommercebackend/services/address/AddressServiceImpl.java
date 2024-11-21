package com.code.ecommercebackend.services.address;

import com.code.ecommercebackend.models.Address;
import com.code.ecommercebackend.repositories.AddressRepository;
import com.code.ecommercebackend.services.BaseServiceImpl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl extends BaseServiceImpl<Address, String> implements AddressService{
    private final AddressRepository addressRepository;

    public AddressServiceImpl(MongoRepository<Address, String> repository, AddressRepository addressRepository) {
        super(repository);
        this.addressRepository = addressRepository;
    }

    @Override
    public Address getAddress() {
        return addressRepository.findAll().stream().findFirst().orElse(null);
    }
}
