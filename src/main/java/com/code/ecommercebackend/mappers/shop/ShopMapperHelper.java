package com.code.ecommercebackend.mappers.shop;

import com.code.ecommercebackend.dtos.request.address.AddressDto;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.mappers.address.AddressMapper;
import com.code.ecommercebackend.models.Address;
import com.code.ecommercebackend.utils.S3Upload;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ShopMapperHelper {
    private final AddressMapper addressMapper;
    private final S3Upload s3Upload;

    @Named("mapAddress")
    public Address mapAddress(AddressDto addressDto) {
        if (addressDto == null) {
            return null;
        }
        return addressMapper.toAddress(addressDto);
    }

    @Named("uploadImage")
    public String uploadImage(MultipartFile image) throws FileNotSupportedException, FileTooLargeException, IOException {
       return s3Upload.uploadImage(image);
    }
}
