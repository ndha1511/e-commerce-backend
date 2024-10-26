package com.code.ecommercebackend.dtos.request.product;

import com.code.ecommercebackend.models.Inventory;
import com.code.ecommercebackend.models.ProductAttribute;
import com.code.ecommercebackend.models.Tag;
import com.code.ecommercebackend.models.Variant;
import lombok.Getter;
import lombok.Setter;


import java.util.List;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
public class ProductExcel {
    private String id;
    private String productName;
    private Set<String> categories;
    private String brandId;
    private String thumbnail;
    private String description;
    private List<Tag> tags;
    private List<String> images;
    private double regularPrice;
    private List<ProductAttribute> attributes;
    private List<VariantExcel> variants;
    private int totalQuantity;
    private int weight;

    public void init(ProductExcel productExcel) {
        this.id = productExcel.getId();
        this.productName = productExcel.getProductName();
        this.categories = productExcel.getCategories();
        this.brandId = productExcel.getBrandId();
        this.thumbnail = productExcel.getThumbnail();
        this.description = productExcel.getDescription();
        this.tags = productExcel.getTags();
        this.images = productExcel.getImages();
        this.regularPrice = productExcel.getRegularPrice();
        this.attributes = productExcel.getAttributes();
        this.variants = productExcel.getVariants();
        this.weight = productExcel.getWeight();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductExcel that = (ProductExcel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


}
