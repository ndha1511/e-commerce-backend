package com.code.ecommercebackend.services.product;

import com.code.ecommercebackend.dtos.request.product.CreateProductRequest;
import com.code.ecommercebackend.dtos.request.product.ProductAttributeDto;
import com.code.ecommercebackend.dtos.request.product.VariantDto;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.mappers.product.ProductMapper;
import com.code.ecommercebackend.mappers.product.attribute.ProductAttributeMapper;
import com.code.ecommercebackend.mappers.product.variant.VariantMapper;
import com.code.ecommercebackend.models.*;
import com.code.ecommercebackend.models.enums.MediaType;
import com.code.ecommercebackend.repositories.ProductAttributeRepository;
import com.code.ecommercebackend.repositories.ProductDetailRepository;
import com.code.ecommercebackend.repositories.VariantRepository;
import com.code.ecommercebackend.services.BaseServiceImpl;
import com.code.ecommercebackend.utils.S3Upload;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl extends BaseServiceImpl<Product, String> implements ProductService {
    private final ProductMapper productMapper;
    private final S3Upload s3Upload;
    private final ProductDetailRepository productDetailRepository;
    private final ProductAttributeRepository productAttributeRepository;
    private final VariantMapper variantMapper;
    private final VariantRepository variantRepository;
    private final ProductAttributeMapper productAttributeMapper;

    public ProductServiceImpl(
            MongoRepository<Product, String> repository,
            ProductMapper productMapper,
            S3Upload s3Upload, ProductDetailRepository productDetailRepository, ProductAttributeRepository productAttributeRepository, VariantMapper variantMapper, VariantRepository variantRepository, ProductAttributeMapper productAttributeMapper) {
        super(repository);
        this.productMapper = productMapper;
        this.s3Upload = s3Upload;
        this.productDetailRepository = productDetailRepository;
        this.productAttributeRepository = productAttributeRepository;
        this.variantMapper = variantMapper;
        this.variantRepository = variantRepository;
        this.productAttributeMapper = productAttributeMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Product save(CreateProductRequest productDto) throws FileTooLargeException, FileNotSupportedException, IOException, DataNotFoundException {
        Product product = productMapper.toProduct(productDto);
        String productId = ObjectId.get().toString();
        product.setId(productId);
        product.createUrlPath();
        product.setTotalQuantity(productDto.getVariantsDto().stream().mapToInt(VariantDto::getQuantity).sum());
        saveProductDetails(productDto, product);
        saveProductAttribute(productDto, productId);
        saveProductVariant(productDto, product);

        return super.save(product);
    }

    private void saveProductDetails(CreateProductRequest productDto, Product product) throws FileTooLargeException, FileNotSupportedException, IOException {
        ProductDetail productDetail = new ProductDetail();
        productDetail.setProductId(product.getId());
        productDetail.setTags(productDto.getTag());
        productDetail.setDescription(productDto.getDescription());
        List<MultipartFile> images = productDto.getImages();
        List<ProductImage> productImages = new ArrayList<>();
        if(images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                String url = s3Upload.uploadImage(image);
                ProductImage productImage = new ProductImage();
                productImage.setMediaType(MediaType.IMAGE);
                productImage.setPath(url);
                productImages.add(productImage);
            }
            product.setThumbnail(productImages.get(productDto.getThumbnailIndex()).getPath());
        }
        productDetail.setImages(productImages);
        productDetailRepository.save(productDetail);
    }

    private void saveProductAttribute(CreateProductRequest productDto, String productId) throws FileTooLargeException, FileNotSupportedException, IOException {
        List<ProductAttributeDto> productAttributesDto = productDto.getAttributesDto();
        List<ProductAttribute> attributes = new ArrayList<>();
        if(productAttributesDto != null && !productAttributesDto.isEmpty()) {
            for (ProductAttributeDto productAttributeDto : productAttributesDto) {
                ProductAttribute productAttribute = productAttributeMapper.toProductAttribute(productAttributeDto);
                productAttribute.setProductId(productId);
                attributes.add(productAttribute);
            }
            productAttributeRepository.saveAll(attributes);
        }
    }

    private void saveProductVariant(CreateProductRequest productDto, Product product) {
        List<VariantDto> variantsDto = productDto.getVariantsDto();
        if(variantsDto != null && !variantsDto.isEmpty()) {
            List<Variant> variants = new ArrayList<>();
            for(VariantDto variantDto : variantsDto) {
                Variant variant = variantMapper.toVariant(variantDto);
                variant.setProduct(product);
                variants.add(variant);
            }
            variantRepository.saveAll(variants);
        }
    }
}
