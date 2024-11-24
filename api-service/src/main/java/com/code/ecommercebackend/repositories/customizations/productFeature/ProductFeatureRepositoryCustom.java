package com.code.ecommercebackend.repositories.customizations.productFeature;

import java.util.List;

public interface ProductFeatureRepositoryCustom {
    List<CategoryCount> findLargestCategoryGroups(long userId);
}
