package com.code.ecommercebackend.services.attribute;

import com.code.ecommercebackend.dtos.request.attribute.AttributeDto;
import com.code.ecommercebackend.dtos.request.attribute.AttributeValueDto;
import com.code.ecommercebackend.dtos.request.attribute.CreateAttributeRequest;
import com.code.ecommercebackend.dtos.request.attribute.VariantDto;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.exceptions.DataNotMatchedException;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.mappers.attribute.AttributeMapper;
import com.code.ecommercebackend.mappers.attribute.VariantMapper;
import com.code.ecommercebackend.models.Product;
import com.code.ecommercebackend.models.ProductAttribute;
import com.code.ecommercebackend.models.Variant;
import com.code.ecommercebackend.repositories.ProductAttributeRepository;
import com.code.ecommercebackend.repositories.ProductRepository;
import com.code.ecommercebackend.repositories.VariantRepository;
import com.code.ecommercebackend.services.BaseServiceImpl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AttributeServiceImpl extends BaseServiceImpl<ProductAttribute, String> implements AttributeService {
    private final AttributeMapper attributeMapper;
    private final ProductAttributeRepository productAttributeRepository;
    private final VariantRepository variantRepository;
    private final VariantMapper variantMapper;
    private final ProductRepository productRepository;

    public AttributeServiceImpl(MongoRepository<ProductAttribute, String> repository,
                                AttributeMapper attributeMapper,
                                ProductAttributeRepository productAttributeRepository, VariantRepository variantRepository, VariantMapper variantMapper, ProductRepository productRepository) {
        super(repository);
        this.attributeMapper = attributeMapper;
        this.productAttributeRepository = productAttributeRepository;
        this.variantRepository = variantRepository;
        this.variantMapper = variantMapper;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(CreateAttributeRequest createAttributeRequest)
            throws DataNotMatchedException, FileTooLargeException, FileNotSupportedException, IOException, DataNotFoundException {
        List<AttributeDto> attributesDto = createAttributeRequest.getAttributes();
        if(attributesDto.size() > 2) throw new DataNotMatchedException("only 2 attributes allowed");
        saveVariant(createAttributeRequest.getVariants(), createAttributeRequest.getAttributes(),
                createAttributeRequest.getProductId());
        List<ProductAttribute> productAttributes = new ArrayList<>();
        for (AttributeDto attributeDto : attributesDto) {
            ProductAttribute productAttribute = attributeMapper.toProductAttribute(attributeDto);
            productAttribute.setProductId(createAttributeRequest.getProductId());
            productAttributes.add(productAttribute);
        }
        productAttributeRepository.saveAll(productAttributes);
    }

    @Override
    public List<ProductAttribute> findByProductId(String productId) {
        return productAttributeRepository.findByProductId(productId);
    }

    private void saveVariant(List<VariantDto> variantsDto, List<AttributeDto> attributesDto, String productId) throws DataNotMatchedException, DataNotFoundException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Product not found"));
        for (AttributeDto attributeDto : attributesDto) {
            List<AttributeValueDto> attributeValuesDto = attributeDto.getAttributeValues();
            for (VariantDto variantDto : variantsDto) {
                long count = attributeValuesDto.stream().filter(
                        attributeVale ->
                                Objects.equals(attributeVale.getValue(), variantDto.getAttributeValue1()) ||
                                        Objects.equals(attributeVale.getValue(), variantDto.getAttributeValue2())
                ).count();

                if(count == 0) {
                    throw new DataNotMatchedException("variant not match");
                }
            }
        }
        List<Variant> variants = variantsDto.stream().map(variantMapper::toVariant).toList();
        variants.forEach(v -> v.setProduct(product));
        variantRepository.saveAll(variants);
    }
}
