package com.code.ecommercebackend.repositories.customizations.inventory;

import com.code.ecommercebackend.models.InventoryDetail;

import java.util.List;

public interface InventoryCustomRepository {
    List<InventoryDetail> getInventoryByVariantId(String variantId);
}
