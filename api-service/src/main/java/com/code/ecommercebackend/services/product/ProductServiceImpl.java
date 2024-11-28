package com.code.ecommercebackend.services.product;

import com.code.ecommercebackend.dtos.response.PageResponse;
import com.code.ecommercebackend.dtos.response.attribute.AttributeResponse;
import com.code.ecommercebackend.dtos.response.product.ProductResponse;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.mappers.product.ProductMapper;
import com.code.ecommercebackend.models.*;
import com.code.ecommercebackend.repositories.*;
import com.code.ecommercebackend.repositories.customizations.product.ProductRepositoryCustom;
import com.code.ecommercebackend.services.BaseServiceImpl;
import com.code.ecommercebackend.services.common.CommonFunction;
import com.code.ecommercebackend.utils.CookieHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ProductServiceImpl extends BaseServiceImpl<Product, String> implements ProductService {

    private final ProductMapper productMapper;
    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;
    private final ProductAttributeRepository productAttributeRepository;
    private final CookieHandler cookieHandler;
    private final CommonFunction commonFunction;
    private final VariantRepository variantRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductFeatureRepository productFeatureRepository;
    private final ProductRepositoryCustom productRepositoryCustom;

    public ProductServiceImpl(
            MongoRepository<Product, String> repository,
            ProductMapper productMapper,
            PromotionRepository promotionRepository,
            ProductRepository productRepository,
            ProductAttributeRepository productAttributeRepository,
            CookieHandler cookieHandler,
            VariantRepository variantRepository,
            CommonFunction commonFunction, BrandRepository brandRepository, CategoryRepository categoryRepository, ProductFeatureRepository productFeatureRepository, ProductRepositoryCustom productRepositoryCustom) {
        super(repository);
        this.productMapper = productMapper;
        this.promotionRepository = promotionRepository;
        this.productRepository = productRepository;
        this.productAttributeRepository = productAttributeRepository;
        this.variantRepository = variantRepository;
        this.cookieHandler = cookieHandler;
        this.commonFunction = commonFunction;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
        this.productFeatureRepository = productFeatureRepository;
        this.productRepositoryCustom = productRepositoryCustom;
    }

    @Override
    public Product save(Product product) {
        long numId = productRepository.count() + 1;
        product.setNumId(numId);
        ProductFeature productFeature = new ProductFeature();
        productFeature.setProductId(product.getNumId());
        productFeature.setProductName(product.getProductName());
        if(product.getBrandId() != null) {
            brandRepository.findById(product.getBrandId()).ifPresent(brand -> productFeature.setBrand(brand.getBrandName()));
        }
        List<String> categories = new ArrayList<>(product.getCategories());
        categoryRepository.findById(categories.get(categories.size() - 1)).ifPresent(category -> productFeature.setCategory(category.getCategoryName()));
        productFeature.setPrice(product.getRegularPrice());
        productFeature.setCountView(0);
        productFeatureRepository.save(productFeature);
        return super.save(product);
    }

    @Override
    public PageResponse<ProductResponse> getPageProduct(int pageNo, int size, String[] search, String[] sort,
                                                        String rangeRegularPrice,
                                                        String rangeRating) {
        PageResponse<Product> pageProduct = productRepositoryCustom.getPageData(pageNo, size, search, sort, rangeRegularPrice, rangeRating);
        List<Product> products = pageProduct.getItems();
        List<ProductResponse> productResponses = mapToProductResponses(products);
        PageResponse<ProductResponse> pageResponse = new PageResponse<>();
        pageResponse.setItems(productResponses);
        pageResponse.setPageSize(pageProduct.getPageSize());
        pageResponse.setPageNumber(pageProduct.getPageNumber());
        pageResponse.setTotalPage(pageProduct.getTotalPage());
        return pageResponse;
    }

    @Override
    public ProductResponse findByUrl(String url, HttpServletRequest request) throws DataNotFoundException {
        String token = cookieHandler.getCookie(request, "access_token");
        Product product = productRepository.findByUrlPath(url)
                .orElseThrow(() -> new DataNotFoundException("product not found"));
        commonFunction.saveUserBehavior(token, 2, product.getNumId(), null);
        List<ProductAttribute> attributes = productAttributeRepository.findByProductId(product.getId());
        ProductResponse productResponse = mapToProductResponse(product);
        productResponse.setAttributes(attributes);
        return productResponse;
    }

    @Override
    public AttributeResponse findAttributeByProductId(String productId)  {
        List<ProductAttribute> attributes = productAttributeRepository.findByProductId(productId);
        List<Variant> variants = variantRepository.findAllByProductId(productId);
        AttributeResponse attributeResponse = new AttributeResponse();
        attributeResponse.setAttributes(attributes);
        attributeResponse.setVariants(variants);
        return attributeResponse;
    }

    @Override
    public List<ProductResponse> getProductResponseByNumIds(List<Long> ids) {
        List<Product> products = productRepository.findAllByNumIdIn(ids);
        return mapToProductResponses(products);
    }

    @Override
    public List<ProductResponse> getProductResponseById(Iterable<String> ids) {
        List<Product> products = productRepository.findAllById(ids);
        return mapToProductResponses(products);
    }


    public List<ProductResponse> mapToProductResponses(List<Product> products) {
        List<ProductResponse> productResponses = new java.util.ArrayList<>(products
                .stream().map(productMapper::toProductResponse).toList());
        int index = 0;
        for (Product product : products) {
            List<Promotion> promotions = promotionRepository.findFirstByCurrentDateAndProductId(product.getId(),
                    Sort.by(Sort.Direction.DESC, "startDate"));
            if(!promotions.isEmpty()) {
                Promotion promotion = promotions.get(0);
                ProductResponse newProductResponse = productResponses.get(index);
                newProductResponse.setPromotion(promotion);
                productResponses.set(index, newProductResponse);
            }
            index++;
        }
        return productResponses;
    }

    public ProductResponse mapToProductResponse(Product product) {
        ProductResponse productResponse = productMapper.toProductResponse(product);
        List<Promotion> promotions = promotionRepository.findFirstByCurrentDateAndProductId(product.getId(),
                Sort.by(Sort.Direction.DESC, "startDate"));
        if(!promotions.isEmpty()) {
            Promotion promotion = promotions.get(0);
            productResponse.setPromotion(promotion);
        }
        return productResponse;
    }

}
