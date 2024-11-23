package com.code.ecommercebackend.repositories.customizations.inventory;

import com.code.ecommercebackend.models.Inventory;

import java.util.List;

public interface InventoryCustomRepository {
    List<Inventory> getInventoryByVariantId(String variantId);
}
