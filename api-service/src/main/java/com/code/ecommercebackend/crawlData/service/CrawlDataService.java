package com.code.ecommercebackend.crawlData.service;

import com.code.ecommercebackend.components.LocalDateTimeVN;
import com.code.ecommercebackend.crawlData.model.Option;
import com.code.ecommercebackend.crawlData.model.Product;
import com.code.ecommercebackend.crawlData.model.ProductDetail;
import com.code.ecommercebackend.crawlData.model.ProductImage;
import com.code.ecommercebackend.crawlData.res.TikiRes;
import com.code.ecommercebackend.models.*;
import com.code.ecommercebackend.models.enums.InventoryStatus;
import com.code.ecommercebackend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CrawlDataService {
    private final RestTemplate restTemplate;
    private final ProductRepository productRepository;
    private final ProductAttributeRepository productAttributeRepository;
    private final VariantRepository variantRepository;

    private final InventoryRepository inventoryRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    Random random = new Random();

    public TikiRes getProducts(String url, String[] brands,Set<String> categories, long venue, int quantity) {
        TikiRes tikiRes = restTemplate.getForObject(url, TikiRes.class);
        assert tikiRes != null;
        List<Product> products = tikiRes.getData();
        for (Product product : products) {
            ProductDetail productDetail = getProductDetail(product.getId() + "");
            com.code.ecommercebackend.models.Product myProduct =
                    new com.code.ecommercebackend.models.Product();
            myProduct.setProductName(product.getName());
            myProduct.setNumId(product.getId());
            String selectedBrand = brands[random.nextInt(brands.length)];
            myProduct.setBrandId(selectedBrand);
            myProduct.createUrlPath();
            myProduct.setVideo(productDetail.getVideoUrl());
            myProduct.normalizerName();
            myProduct.setCategories(categories);
            myProduct.setRegularPrice(product.getPrice());
            myProduct.setThumbnail(product.getThumbnailUrl());
            myProduct.setDescription(productDetail.getDescription());
            List<ProductImage> productImages = productDetail.getImages();
            List<String> imagesUrl = productImages.stream().map(ProductImage::getBaseUrl).toList();
            myProduct.setImages(imagesUrl);
            try {
                productRepository.save(myProduct);
            } catch (Exception e) {
                continue;
            }

            List<Option> options = productDetail.getConfigurableOptions();
            if(options != null && !options.isEmpty()) {
                // attribute 1
                ProductAttribute productAttribute = new ProductAttribute();
                productAttribute.setProductId(myProduct.getId());
                productAttribute.setAttributeName(options.get(0).getName());
                productAttribute.setAttributeValues(options.get(0).getValues().stream()
                        .map(v -> new AttributeValue(v.getLabel())).toList());

                // attribute 2
                ProductAttribute productAttribute2 = null;
                if(options.size() > 1) {
                    productAttribute2 = new ProductAttribute();
                    productAttribute2.setProductId(myProduct.getId());
                    productAttribute2.setAttributeName(options.get(1).getName());
                    productAttribute2.setAttributeValues(options.get(1).getValues().stream()
                            .map(v -> new AttributeValue(v.getLabel())).toList());
                }
                productAttributeRepository.save(productAttribute);
                if(productAttribute2 != null) {
                    productAttributeRepository.save(productAttribute2);
                }


                // variant inventory
                int totalQuantity = 0;
                double totalPrice = 0;
                Set<String> inventoryIds = new HashSet<>();
                for (int j = 0; j < productAttribute.getAttributeValues().size(); j++) {
                    if(productAttribute2 != null) {
                        for (int k = 0; k < productAttribute2.getAttributeValues().size(); k++) {
                            Variant variant = new Variant();
                            variant.setAttributeValue1(productAttribute.getAttributeValues().get(j).getValue());
                            variant.setAttributeValue2(productAttribute2.getAttributeValues().get(k).getValue());
                            variant.setPrice(product.getPrice());
                            variant.setProduct(myProduct);
                            variantRepository.save(variant);
                            Inventory inventory = new Inventory();
                            inventory.setProductId(myProduct.getId());
                            inventory.setVariantId(variant.getId());
                            inventory.setInventoryStatus(InventoryStatus.IN_STOCK);
                            inventory.setImportPrice(Math.max(product.getPrice() - venue, 10000));
                            inventory.setImportQuantity(quantity);
                            inventory.setImportDate(LocalDateTimeVN.now());
                            inventoryRepository.save(inventory);
                            inventoryIds.add(inventory.getId());
                            totalQuantity += quantity;
                            totalPrice += Math.max(product.getPrice() - venue, 10000) * quantity;
                        }
                    } else {
                        Variant variant = new Variant();
                        variant.setAttributeValue1(productAttribute.getAttributeValues().get(j).getValue());
                        variant.setPrice(product.getPrice());
                        variant.setProduct(myProduct);
                        variantRepository.save(variant);
                        Inventory inventory = new Inventory();
                        inventory.setProductId(myProduct.getId());
                        inventory.setVariantId(variant.getId());
                        inventory.setInventoryStatus(InventoryStatus.IN_STOCK);
                        inventory.setImportPrice(Math.max(product.getPrice() - venue, 10000));
                        inventory.setImportQuantity(quantity);
                        inventory.setImportDate(LocalDateTimeVN.now());
                        inventoryRepository.save(inventory);
                        inventoryIds.add(inventory.getId());
                        totalQuantity += quantity;
                        totalPrice += Math.max(product.getPrice() - venue, 10000) * quantity;
                    }

                }

                if(options.isEmpty()) {
                    Variant variant = new Variant();
                    variant.setProduct(myProduct);
                    variant.setPrice(product.getPrice());
                    variantRepository.save(variant);
                    Inventory inventory = new Inventory();
                    inventory.setProductId(myProduct.getId());
                    inventory.setVariantId(variant.getId());
                    inventory.setInventoryStatus(InventoryStatus.IN_STOCK);
                    inventory.setImportPrice(Math.max(product.getPrice() - venue, 10000));
                    inventory.setImportQuantity(quantity);
                    inventory.setImportDate(LocalDateTimeVN.now());
                    inventoryRepository.save(inventory);
                    inventoryIds.add(inventory.getId());
                    totalQuantity += quantity;
                    totalPrice += Math.max(product.getPrice() - venue, 10000) * quantity;
                }

                myProduct.setTotalQuantity(totalQuantity);
                productRepository.save(myProduct);

                // purchase order
                PurchaseOrder purchaseOrder = new PurchaseOrder();
                purchaseOrder.setImportStaffName("Admin");
                purchaseOrder.setOrderDate(LocalDateTimeVN.now());
                purchaseOrder.setInventories(inventoryIds);
                purchaseOrder.setTotalQuantity(totalQuantity);
                purchaseOrder.setTotalPrice(totalPrice);

                purchaseOrderRepository.save(purchaseOrder);
            } else {
                Inventory inventory = new Inventory();
                inventory.setProductId(myProduct.getId());
                inventory.setImportDate(LocalDateTimeVN.now());
                inventory.setInventoryStatus(InventoryStatus.IN_STOCK);
                inventory.setImportPrice(Math.min(product.getPrice() - venue, 10000));
                inventory.setImportQuantity(quantity);
                inventoryRepository.save(inventory);

                PurchaseOrder purchaseOrder = new PurchaseOrder();
                purchaseOrder.setImportStaffName("Admin");
                purchaseOrder.setOrderDate(LocalDateTimeVN.now());
                purchaseOrder.setInventories(Set.of(inventory.getId()));
                purchaseOrder.setTotalPrice(Math.min(product.getPrice() - venue, 10000) * quantity);
                purchaseOrder.setTotalQuantity(quantity);
                purchaseOrderRepository.save(purchaseOrder);
                myProduct.setTotalQuantity(quantity);
                productRepository.save(myProduct);
            }



        }

        return tikiRes;
    }

    public TikiRes previewData(String url) {
        return restTemplate.getForObject(url, TikiRes.class);
    }

    public ProductDetail getProductDetail(String id) {
        String apiUrl = "https://tiki.vn/api/v2/products/" + id + "?platform=web&version=3";
        return restTemplate.getForObject(apiUrl, ProductDetail.class);
    }
}
